package biker.snailz.entities;

import biker.snailz.AStarPathFinder;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SnailTargetPlayerGoal extends Goal {
    private final SnailEntity snail;
    private Vec3d targetPlayer;
    private Vec3d nonfloortarget;
    private int currentIndex;
    private int currentIndexsnail;
    private int failedpaths;
    private final World world;
    private Vec3d lastTargetPlayerPos;
    private Vec3d lastSnailPos;
    List<AStarPathFinder.Node> path1;
    List<BlockPos> path2 = new ArrayList<>();
    private Random random = new Random();
    private ServerWorld serverWorld;

    public SnailTargetPlayerGoal(SnailEntity snail) {
        this.snail = snail;
        this.targetPlayer = null;
        this.nonfloortarget = null;
        this.world = this.snail.getWorld();


    }
    @Override
    public boolean canStart() {
        targetPlayer = snail.getTargetPlayer(); // Get target player's position
        nonfloortarget = snail.getTargetPlayer2();


        if (targetPlayer == null && nonfloortarget == null) {
            return false;
        }
        return true;
    }
    @Override
    public void start() {
        currentIndex = 0;
        if (path1 != null) {
            path1.clear();
        }
        if (path2 != null) {
            path2.clear();
        }
    }


    private void teleport() {
        System.out.println("teleported");
        System.out.println(distanceTo());
        System.out.println();
        double x = targetPlayer.x + random.nextDouble(10, 25);
        double y = targetPlayer.getY();
        double z = targetPlayer.z + random.nextDouble(10, 25);
        snail.teleport(x, y, z, true);
        currentIndexsnail = 0;
        System.out.println("player coords" + x + " " + y + " " + z + " ");
    }


    @Override
    public void tick() {

        double distance = distanceTo();
        if (distance >= 55) {
           teleport();
        }


        boolean var1 = !snail.getWorld().getBlockState(snail.getBlockPos().down()).isIn(BlockTags.AIR);
        boolean var2 = !snail.getWorld().getBlockState(snail.getBlockPos().up()).isIn(BlockTags.AIR);
        boolean var3 = !snail.getWorld().getBlockState(snail.getBlockPos().north()).isIn(BlockTags.AIR);
        boolean var4 = !snail.getWorld().getBlockState(snail.getBlockPos().south()).isIn(BlockTags.AIR);
        boolean var5 = !snail.getWorld().getBlockState(snail.getBlockPos().east()).isIn(BlockTags.AIR);
        boolean var6 = !snail.getWorld().getBlockState(snail.getBlockPos().west()).isIn(BlockTags.AIR);

        boolean allofem = var1 && var2 && var3 && var4 && var5 && var6;


        if (allofem) {
            teleport();
        }







        if (failedpaths == 25) {
            snail.getNavigation().startMovingTo(nonfloortarget.x, nonfloortarget.y, nonfloortarget.z, 0, SnailEntity.WALKING_SPEED);

            System.out.println("failedpaths reached limit. just gonna try to move to player");
            failedpaths = 0;

            if (random.nextInt(1, 5) == 3) {
                teleport();
            }


        }

        if (lastTargetPlayerPos == null || !lastTargetPlayerPos.equals(targetPlayer)) {

            currentIndex = 0;
            // Update the last known position
            lastTargetPlayerPos = targetPlayer;
            if (path2 != null) {
                path2.clear();

            }
            if (path1 != null){
                path1.clear();
            }
        }

            if (path2.isEmpty()) {

                AStarPathFinder.Node start = new AStarPathFinder.Node((int) snail.getX(), (int) snail.getY(), (int) snail.getZ());
                AStarPathFinder.Node end = new AStarPathFinder.Node((int) targetPlayer.x, (int) targetPlayer.y, (int) targetPlayer.z);
                List<AStarPathFinder.Node> path1 = AStarPathFinder.findPath(start, end, world);

                //System.out.println(snail.getX() + " " + snail.getY()+ " " + snail.getZ());
                //System.out.println(targetPlayer.x + " " + targetPlayer.y + " " + targetPlayer.z);

                for (AStarPathFinder.Node node : path1) {
                BlockPos blockpos = new BlockPos(node.x, node.y, node.z);
                //System.out.println("Nodes: " + node.x + " " + node.y + " " + node.z);
                path2.add(blockpos);
            }
        }

        if (path2 == null || path2.isEmpty()) {
            failedpaths++;
            //System.out.println("path 2 is empty");
            return;
        }

        // please ive been working on this for hours pleasepleaseplease just work
        if (snail.getBlockPos().isWithinDistance(path2.get(currentIndex), 1.05)) {
            currentIndex++;
            if (currentIndex >= path2.size()) {
                currentIndex = 0;
                path2.clear();
                // praying that this will work
                return;
            }
        }

        // JUST GO TO THE PLACE PLEASE
        // gonna try client right now..
        // didnt work exactly :( but I made the block detection less sensitive so thats good.
        // the snail really likes to go to 1 69 -7 idk why

        //omg im getting somewhere, node adding works, and like the snail goes to you but then goes back, which is expected now that I think about it. but I hope I can fix
        // IT WORKS!!!!!

        boolean issnailmoving;
        if (snail.getVelocity().x == 0 && snail.getVelocity().y == 0 && snail.getVelocity().z == 0) {
            issnailmoving = false;
        } else {
            issnailmoving = true;
        }


        double squareddistance = snail.squaredDistanceTo(nonfloortarget.x, nonfloortarget.y ,nonfloortarget.z);


        float i = 6;
        if (path2 == null && !issnailmoving && squareddistance <= i && snail.getY() == targetPlayer.y || snail.getY() == targetPlayer.y - 1 || snail.getY() == targetPlayer.y + 1 || snail.getY() == targetPlayer.y && !issnailmoving && path2 == null) {
            System.out.println("pathfinding cause players close");

            snail.getNavigation().startMovingTo(nonfloortarget.x, nonfloortarget.y, nonfloortarget.z, 0, SnailEntity.WALKING_SPEED);
        }
        else {
            snail.getNavigation().startMovingTo(path2.get(currentIndex).getX(),
                    path2.get(currentIndex).getY(),
                    path2.get(currentIndex).getZ(),
                    0, SnailEntity.WALKING_SPEED); // goinginsanerightnowaaaaaa
            //System.out.println("pathfindin with regular stuffs");
        }



    }

    public float distanceTo() {
        float f = (float)(snail.getX() - targetPlayer.getX());
        float g = (float)(snail.getY() - targetPlayer.getY());
        float h = (float)(snail.getZ() - targetPlayer.getZ());
        return MathHelper.sqrt(f * f + g * g + h * h);
    }


}

  //  @Override
  //  public boolean shouldContinue() {
  //      return targetPlayer != null && pathIndex < pathCoordinates.size();
  //  }

