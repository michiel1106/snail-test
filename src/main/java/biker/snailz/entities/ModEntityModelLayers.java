package biker.snailz.entities;

import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

import static biker.snailz.Snailz.MOD_ID;


public class ModEntityModelLayers {

    private static final String MAIN = "main";
    private static final String INNER_ARMOR = "inner_armor";
    private static final String OUTER_ARMOR = "outer_armor";


    public static final EntityModelLayer SNAIL = createEntityModelLayer("snail", MAIN);



    public static void registerEntityModelLayers() {
        System.out.println("Register entity model layers");
    }

    public static EntityModelLayer createEntityModelLayer(String path, String layer) {
        return new EntityModelLayer(Identifier.of(MOD_ID, path), layer);
    }
}