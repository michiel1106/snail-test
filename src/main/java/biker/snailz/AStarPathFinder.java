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
            //System.out.println("A* Starting pathfinding...");

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
                        //System.out.println("A* Destination reached!" + "it took " + iterations + "iterations");
                        return reconstructPath(cameFrom, current);
                    }

                    closedSet.add(current);

                    // Log current node being processed
                    //System.out.println("Processing node: (" + current.x + ", " + current.y + ", " + current.z + ")");

                    for (Node neighbor : getNeighbors(current, world)) {
                        try {
                            if (closedSet.contains(neighbor)) continue;

                            double tentativeGCost = gCostMap.get(current) + distance(current, neighbor, world);
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
                        //System.out.println(iterations + " iterations completed. A*");
                    }

                    if (iterations > 25000) {
                        //System.out.println("A* Too many iterations, breaking out!");
                        break;
                    }

                } catch (Exception e) {
                    //System.err.println("A* Error processing node (" + current.x + ", " + current.y + ", " + current.z + ")");
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

            // Allow air traversal while still considering ground paths
            if (isWalkable(pos, world) || world.getBlockState(pos).isAir()) {
                neighbors.add(new Node(nx, ny, nz));
            }
        }
        return neighbors;
    }
    private static double heuristic(Node a, Node b) {
        int dx = Math.abs(a.x - b.x); // Horizontal distance in x
        int dz = Math.abs(a.z - b.z); // Horizontal distance in z
        int dy = a.y - b.y; // Vertical distance (can be negative or positive)

        // Strongly prioritize horizontal movement
        double horizontalCost = dx + dz;

        // Penalize upward movement more heavily
        double verticalPenalty = dy > 0 ? dy * 3 : dy * 0.5;

        return horizontalCost + Math.abs(verticalPenalty);
    }

    private static double distance(Node a, Node b, World world) {
        BlockPos pos = new BlockPos(b.x, b.y, b.z);
        BlockState state = world.getBlockState(pos);

        double baseCost = 1.0;

        if (b.y > a.y) baseCost += 0.3; // Slight penalty for upward movement
        if (b.y < a.y) baseCost -= 0.1; // Reward for downward movement

        if (state.isAir()) baseCost += 0.5; // Slight penalty for air movement
        if (!   state.isIn(BlockTags.AIR)) baseCost += 12.8; // Higher cost for breaking through blocks

        return baseCost;
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
            BlockPos twoBelow = below.down(); // Block two blocks below

            // Check if there is a solid block directly under or two blocks under
            //boolean hasSupport = isSolidBlock(below, world) || isSolidBlock(twoBelow, world);

            // Check if the position itself is walkable
            BlockState state = world.getBlockState(pos);
            boolean isEmptyOrPassable = state.isAir() || state.isReplaceable() || state.isOf(Blocks.TALL_GRASS) || state.isOf(Blocks.SHORT_GRASS) || state.isOf(Blocks.CORNFLOWER) || state.isOf(Blocks.DANDELION) || state.isOf(Blocks.POPPY) || state.isOf(Blocks.WATER) || state.isOf(Blocks.LAVA);

            // Node is walkable if it has support below and the position itself is passable
            return true;
        }

        private static boolean isSolidBlock(BlockPos pos, World world) {
            BlockState state = world.getBlockState(pos);
            return !state.isIn(BlockTags.AIR);  // Ensures the block can support an entity
        }
    }

