package com.mapter.kombucha.component;

import com.mapter.kombucha.Kombucha;
import com.mapter.kombucha.block.KombuchaJarBlock;
import com.mapter.kombucha.block.MushroomType;
import com.mapter.kombucha.block.TeaType;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Kombucha.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<KombuchaJarBlock.JarType>> JAR_TYPE =
            DATA_COMPONENT_TYPES.register("jar_type",
                    () -> DataComponentType.<KombuchaJarBlock.JarType>builder()
                            .persistent(KombuchaJarBlock.JarType.CODEC)
                            .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<TeaType>> TEA_TYPE =
            DATA_COMPONENT_TYPES.register("tea_type",
                    () -> DataComponentType.<TeaType>builder()
                            .persistent(TeaType.CODEC)
                            .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<MushroomType>> MUSHROOM_TYPE =
            DATA_COMPONENT_TYPES.register("mushroom_type",
                    () -> DataComponentType.<MushroomType>builder()
                            .persistent(MushroomType.CODEC)
                            .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<LivingShroomData>> LIVING_SHROOM_DATA =
            DATA_COMPONENT_TYPES.register("living_shroom_data",
                    () -> DataComponentType.<LivingShroomData>builder()
                            .persistent(LivingShroomData.CODEC)
                            .build());
}
