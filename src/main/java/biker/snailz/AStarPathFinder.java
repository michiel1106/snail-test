package biker.snailz;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

import static net.minecraft.registry.tag.BlockTags.FLOWERS;


public class AStarPathFinder {

        public static class Node implements Comparable<Node> {

            public int x, y, z;
            public double gCost; // Distance from start node
            public double hCost; // Heuristic (estimated distance to end node)
            public Node parent;

            public Node(int x, int y, int z) {
                this.x = x;
                this.y = y;
                this.z = z;
            }

            public double fCost() {
                return gCost + hCost;
            }

            @Override
            public int compareTo(Node other) {
                return Double.compare(this.fCost(), other.fCost());
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null || getClass() != obj.getClass()) return false;
                Node node = (Node) obj;
                return x == node.x && y == node.y && z == node.z;
            }

            @Override
            public int hashCode() {
                return Objects.hash(x, y, z);
            }
        }

        public static List<Node> findPath(Node start, Node end, World world) {
            System.out.println("A* Starting pathfinding...");

            PriorityQueue<Node> openSet = new PriorityQueue<>();
            Set<Node> closedSet = new HashSet<>();
            Map<Node, Double> gCostMap = new HashMap<>();
            Map<Node, Node> cameFrom = new HashMap<>();

            start.gCost = 0;
            start.hCost = heuristic(start, end);
            openSet.add(start);
            gCostMap.put(start, 0.0);

            int iterations = 0;
            while (!openSet.isEmpty()) {
                Node current = openSet.poll();

                try {
                    if (current.equals(end)) {
                        System.out.println("A* Destination reached!" + "it took " + iterations + "iterations");
                        return reconstructPath(cameFrom, current);
                    }

                    closedSet.add(current);

                    // Log current node being processed
                    //System.out.println("Processing node: (" + current.x + ", " + current.y + ", " + current.z + ")");

                    for (Node neighbor : getNeighbors(current, world)) {
                        try {
                            if (closedSet.contains(neighbor)) continue;

                            double tentativeGCost = gCostMap.get(current) + distance(current, neighbor);
                            if (!gCostMap.containsKey(neighbor) || tentativeGCost < gCostMap.get(neighbor)) {
                                gCostMap.put(neighbor, tentativeGCost);
                                neighbor.hCost = heuristic(neighbor, end);
                                cameFrom.put(neighbor, current);

                                if (!openSet.contains(neighbor)) {
                                    openSet.add(neighbor);
                                } else {
                                    openSet.remove(neighbor);
                                    openSet.add(neighbor); // Update priority
                                }
                            }
                        } catch (Exception e) {
                            //System.err.println("Error processing neighbor (" + neighbor.x + ", " + neighbor.y + ", " + neighbor.z + ")");
                            //e.printStackTrace();
                        }
                    }

                    iterations++;
                    if (iterations % 50000 == 0) {
                        System.out.println(iterations + " iterations completed. A*");
                    }

                    if (iterations > 25000) {
                        System.out.println("A* Too many iterations, breaking out!");
                        break;
                    }

                } catch (Exception e) {
                    System.err.println("A* Error processing node (" + current.x + ", " + current.y + ", " + current.z + ")");
                    e.printStackTrace();
                    break; // Exit to prevent infinite looping or further errors
                }
            }

            //System.out.println("No path found!");
            return Collections.emptyList();
        }

        private static List<Node> getNeighbors(Node node, World world) {
            List<Node> neighbors = new ArrayList<>();
            int[][] directions = {
                    {1, 0, 0}, {-1, 0, 0}, {0, 1, 0}, {0, -1, 0}, {0, 0, 1}, {0, 0, -1}
            };

            for (int[] dir : directions) {
                int nx = node.x + dir[0];
                int ny = node.y + dir[1];
                int nz = node.z + dir[2];

                BlockPos pos = new BlockPos(nx, ny, nz);
                try {
                    if (isWalkable(pos, world)) {
                        neighbors.add(new Node(nx, ny, nz));
                    } else {
                        //System.out.println("Non-walkable position: (" + nx + ", " + ny + ", " + nz + ")");
                    }
                } catch (Exception e) {
                    System.err.println("A* Error determining walkability for position: (" + nx + ", " + ny + ", " + nz + ")");
                    e.printStackTrace();
                }
            }
            return neighbors;
        }
        private static double heuristic(Node a, Node b) {
            int dx = Math.abs(a.x - b.x); // Difference in x
            int dz = Math.abs(a.z - b.z); // Difference in z
            int dy = a.y - b.y; // Vertical difference (not absolute to differentiate up/down)

            // Penalize upward movement (dy > 0), encourage downward (dy < 0)
            double verticalPenalty = dy > 0 ? dy * 1.5 : dy * 0.5;

            return dx + dz + Math.abs(dy) + verticalPenalty;


        }

        private static double distance(Node a, Node b) {
            return 1.0;
        }

        private static List<Node> reconstructPath(Map<Node, Node> cameFrom, Node current) {
            List<Node> path = new ArrayList<>();
            while (current != null) {
                path.add(current);
                current = cameFrom.get(current);
            }
            Collections.reverse(path);
            return path;
        }

        private static boolean isWalkable(BlockPos pos, World world) {
            BlockPos below = pos.down();        // Block directly below
            BlockPos twoBelow = below.down();  // Block two blocks below

            // Check if there is a solid block directly under or two blocks under
            boolean hasSupport = isSolidBlock(below, world) || isSolidBlock(twoBelow, world);

            // Check if the position itself is walkable
            BlockState state = world.getBlockState(pos);
            boolean isEmptyOrPassable = state.isAir() || state.isReplaceable() || state.isOf(Blocks.TALL_GRASS) || state.isOf(Blocks.SHORT_GRASS) || state.isOf(Blocks.CORNFLOWER) || state.isOf(Blocks.DANDELION) || state.isOf(Blocks.POPPY);

            // Node is walkable if it has support below and the position itself is passable
            return hasSupport && isEmptyOrPassable;
        }

        private static boolean isSolidBlock(BlockPos pos, World world) {
            BlockState state = world.getBlockState(pos);
            return !state.isIn(BlockTags.AIR);  // Ensures the block can support an entity
        }
    }

