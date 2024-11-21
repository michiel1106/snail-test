package biker.snailz;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

@SuppressWarnings("ALL")
public class PathCommand {
    private static World world;

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("path")
                        .requires(source -> source.hasPermissionLevel(2)) // Requires operator-level permissions
                        .then(
                                CommandManager.argument("from", BlockPosArgumentType.blockPos())
                                        .then(
                                                CommandManager.argument("to", BlockPosArgumentType.blockPos())
                                                        .executes(PathCommand::execute)
                                        )
                        )
        );
    }

    private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        ServerCommandSource source = context.getSource();
        World world = source.getWorld();  // Get the world from the source

        BlockPos from = BlockPosArgumentType.getLoadedBlockPos(context, "from");
        BlockPos to = BlockPosArgumentType.getLoadedBlockPos(context, "to");

        // Log received positions
        System.out.println("Path command received!");
        System.out.println("From: " + from + " To: " + to);

        // Create start and end nodes for A* pathfinding
        AStarPathFinder.Node start = new AStarPathFinder.Node(from.getX(), from.getY(), from.getZ());
        AStarPathFinder.Node end = new AStarPathFinder.Node(to.getX(), to.getY(), to.getZ());;

        System.out.println("Starting pathfinding...");

        // Find the path, passing the world to ensure walkability checks
        List<AStarPathFinder.Node> path = AStarPathFinder.findPath(start, end, world);

        System.out.println(path);

        System.out.println("Finished pathfinding! Path size: " + path.size());

        // If no path is found, log and return
        if (path.isEmpty()) {
            System.out.println("No path found!");
            return 0;
        }

        // Log that path blocks will be placed
        System.out.println("Path found! Placing blocks...");

        // Place blocks along the found path
        for (AStarPathFinder.Node node : path) {
            BlockPos pos = new BlockPos(node.x, node.y, node.z);

            // Log the current position being processed
            System.out.println("Checking block at: " + pos);

            if (world.getBlockState(pos).isAir()) {
                // If the block is air, place the block (example: emerald block)
                world.setBlockState(pos, Blocks.EMERALD_BLOCK.getDefaultState());
                System.out.println("Placed block at: " + pos);
            } else {
                // Log if the block is not air, and we skip placing a block
                System.out.println("Block at " + pos + " is not air, skipping.");
            }
        }

        // Log the completion of the block placement process
        System.out.println("Path completed!");


        return 1;
    }
}

