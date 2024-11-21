package biker.snailz;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;




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

        /**
         * Find the optimal path using the A* algorithm.
         *
         * @param start Starting node
         * @param end   Ending node
         * @param world The world in which pathfinding will be done
         * @return List of nodes representing the optimal path, or an empty list if no path is found
         */
        public static List<Node> findPath(Node start, Node end, World world) {
            System.out.println("Starting pathfinding...");

            // Clear any previous states
            PriorityQueue<Node> openSet = new PriorityQueue<>();
            Set<Node> closedSet = new HashSet<>();

            start.gCost = 0;
            start.hCost = heuristic(start, end);
            openSet.add(start);

            int iterations = 0;  // Add an iteration counter to avoid infinite loops

            while (!openSet.isEmpty()) {
                Node current = openSet.poll();

                // Debug prints
                System.out.println("Current node: " + current.x + ", " + current.y + ", " + current.z);
                System.out.println("Open set size: " + openSet.size());
                iterations++;
                if (iterations > 1000) {  // Add a limit to iterations to prevent infinite loops
                    System.out.println("Too many iterations, breaking out!");
                    break;
                }

                // If we've reached the destination node, reconstruct the path
                if (current.equals(end)) {
                    System.out.println("Destination reached!");
                    return reconstructPath(current);
                }

                closedSet.add(current);

                // Get neighbors and process them
                for (Node neighbor : getNeighbors(current, world)) {
                    BlockPos neighborPos = new BlockPos(neighbor.x, neighbor.y, neighbor.z);
                    if (!world.getBlockState(neighborPos).isAir()) {
                        continue;  // Skip non-walkable blocks
                    }

                    double tentativeGCost = current.gCost + distance(current, neighbor);
                    if (!openSet.contains(neighbor) || tentativeGCost < neighbor.gCost) {
                        neighbor.gCost = tentativeGCost;
                        neighbor.hCost = heuristic(neighbor, end);
                        neighbor.parent = current;

                        if (!openSet.contains(neighbor)) {
                            openSet.add(neighbor);
                        }
                    }
                }
            }

            System.out.println("No path found!");
            return Collections.emptyList();  // Return empty list if no path is found
        }
        // Get all valid neighbors for a node
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
                if (isWalkable(pos, world)) {
                    neighbors.add(new Node(nx, ny, nz));
                }
            }
            return neighbors;
        }

        // Heuristic function (Manhattan distance)
        private static double heuristic(Node a, Node b) {
            return Math.abs(a.x - b.x) + Math.abs(a.y - b.y) + Math.abs(a.z - b.z);
        }

        // Distance between two nodes (1.0 for adjacent nodes)
        private static double distance(Node a, Node b) {
            return 1.0;
        }

        // Reconstruct the path from the end node to the start node
        private static List<Node> reconstructPath(Node end) {
            List<Node> path = new ArrayList<>();
            Node current = end;
            while (current != null) {
                path.add(current);
                current = current.parent;
            }
            Collections.reverse(path);
            return path;
        }

        // Check if a block at a given position is walkable (e.g., air)
        private static boolean isWalkable(BlockPos pos, World world) {
            BlockState state = world.getBlockState(pos);
            return state.isAir();  // You can expand this check with more block types that are walkable.
        }
    }


