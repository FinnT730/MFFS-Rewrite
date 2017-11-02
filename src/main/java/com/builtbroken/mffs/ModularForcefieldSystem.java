package com.builtbroken.mffs;

import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.framework.mod.AbstractMod;
import com.builtbroken.mc.framework.mod.AbstractProxy;
import com.builtbroken.mffs.content.fluids.Fortron;
import com.builtbroken.mffs.common.items.ItemFocusMatrix;
import com.builtbroken.mffs.common.items.RemoteController;
import com.builtbroken.mffs.common.items.card.*;
import com.builtbroken.mffs.common.items.card.id.ItemCardID;
import com.builtbroken.mffs.common.items.modules.interdiction.*;
import com.builtbroken.mffs.common.items.modules.projector.*;
import com.builtbroken.mffs.common.items.modules.projector.mode.*;
import com.builtbroken.mffs.common.items.modules.upgrades.*;
import com.builtbroken.mffs.common.net.packet.*;
import com.builtbroken.mffs.content.biometric.BlockBiometricIdentifier;
import com.builtbroken.mffs.content.interdiction.BlockInterdictionMatrix;
import com.builtbroken.mffs.content.biometric.TileBiometricIdentifier;
import com.builtbroken.mffs.content.cap.BlockFortronCapacitor;
import com.builtbroken.mffs.content.cap.TileFortronCapacitor;
import com.builtbroken.mffs.content.field.BlockForceField;
import com.builtbroken.mffs.content.field.TileForceField;
import com.builtbroken.mffs.content.gen.BlockCoercionDeriver;
import com.builtbroken.mffs.content.gen.TileCoercionDeriver;
import com.builtbroken.mffs.content.interdiction.TileInterdictionMatrix;
import com.builtbroken.mffs.content.projector.BlockForceFieldProjector;
import com.builtbroken.mffs.content.projector.TileForceFieldProjector;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;

@Mod(modid = ModularForcefieldSystem.DOMAIN, name = ModularForcefieldSystem.MOD_NAME, version = ModularForcefieldSystem.VERSION, dependencies = "required-after:voltzengine")
public class ModularForcefieldSystem extends AbstractMod
{
    public static final String DOMAIN = "mffs";
    public static final String VERSION = "0.50";
    public static final String MOD_NAME = "Modular Forcefield System";


    //Blocks
    public static Block biometricIdentifier;
    public static Block coercionDeriver;
    public static Block forcefieldProjector;
    public static Block fortronCapacitor;
    public static Block interdictionMatrix;

    //Items
    public static ItemCardID itemCardID;
    /**
     * Constructor.
     */
    public ModularForcefieldSystem()
    {
        super(DOMAIN, DOMAIN + "/main");
        manager.defaultTab = new CreativeTabs(ModularForcefieldSystem.DOMAIN)
        {

            @Override
            @SideOnly(Side.CLIENT)
            public Item getTabIconItem()
            {
                return Item.getItemFromBlock(forcefieldProjector);
            }
        };
    }

    @Mod.Instance
    public static ModularForcefieldSystem modularForcefieldSystem_mod;

    @SidedProxy(clientSide = "com.builtbroken.mffs.client.ClientProxy", serverSide = "com.builtbroken.mffs.CommonProxy")
    public static CommonProxy proxy;

    /* This is the communication channel of the mod */
    public static SimpleNetworkWrapper channel;

    @Override
    public AbstractProxy getProxy()
    {
        return proxy;
    }

    @Override
    protected void loadBlocks(ModManager manager)
    {
        biometricIdentifier = manager.newBlock(BlockBiometricIdentifier.class).setBlockName("biometricIdentifier");
        coercionDeriver = manager.newBlock(BlockCoercionDeriver.class).setBlockName("coercionDeriver");
        BlockForceField.BLOCK_FORCE_FIELD = (BlockForceField) manager.newBlock(BlockForceField.class).setBlockName("forceField").setCreativeTab(null);
        forcefieldProjector = manager.newBlock(BlockForceFieldProjector.class).setBlockName("forceFieldProjector");
        fortronCapacitor = manager.newBlock(BlockFortronCapacitor.class).setBlockName("fortronCapacitor");
        interdictionMatrix = manager.newBlock(BlockInterdictionMatrix.class).setBlockName("interdictionMatrix");
    }

    @Override
    public void loadItems(ModManager manager)
    {
        manager.newItem("cardBlank", ItemCardBlank.class); //TODO Fix this mess, redo IDs, try to merge into one item using JSON system
        manager.newItem("cardFrequency", ItemCardFrequency.class);
        itemCardID = manager.newItem("cardID", ItemCardID.class);
        manager.newItem("cardInfinite", ItemCardInfinite.class);
        manager.newItem("cardLink", ItemCardLink.class);
        manager.newItem("focusMatrix", ItemFocusMatrix.class);
        manager.newItem("moduleAntiFriendly", ItemModuleAntiFriendly.class);
        manager.newItem("moduleAntiHostile", ItemModuleAntiHostile.class);
        manager.newItem("moduleAntiPersonnel", ItemModuleAntiPersonnel.class);
        manager.newItem("moduleAntiSpawn", ItemModuleAntiSpawn.class);
        manager.newItem("moduleBlockAccess", ItemModuleBlockAccess.class);
        manager.newItem("moduleBlockAlter", ItemModuleBlockAlter.class);
        manager.newItem("moduleConfiscate", ItemModuleConfiscate.class);
        manager.newItem("moduleRepulsion", ItemModuleRepulsion.class);
        manager.newItem("moduleWarn", ItemModuleWarn.class);
        manager.newItem("moduleApproximation", ItemModuleApproximation.class);
        manager.newItem("moduleArray", ItemModuleArray.class);
        manager.newItem("moduleCamouflage", ItemModuleCamouflage.class);
        manager.newItem("moduleCollection", ItemModuleCollection.class);
        manager.newItem("moduleDisintegration", ItemModuleDisintegration.class);
        manager.newItem("moduleDome", ItemModuleDome.class);
        manager.newItem("moduleFusion", ItemModuleFusion.class);
        manager.newItem("moduleGlow", ItemModuleGlow.class);
        manager.newItem("moduleInvert", ItemModuleInvert.class);
        manager.newItem("moduleShock", ItemModuleShock.class);
        manager.newItem("moduleSilence", ItemModuleSilence.class);
        manager.newItem("moduleStabilize", ItemModuleStabilize.class);
        manager.newItem("modeCube", ItemModeCube.class);
        manager.newItem("modeCustom", ItemModeCustom.class);
        manager.newItem("modeCylinder", ItemModeCylinder.class);
        manager.newItem("modePyramid", ItemModePyramid.class);
        manager.newItem("modeSphere", ItemModeSphere.class);
        manager.newItem("modeTube", ItemModeTube.class);
        manager.newItem("moduleCapacity", ItemModuleCapacity.class);
        manager.newItem("moduleRotate", ItemModuleRotate.class);
        manager.newItem("moduleScale", ItemModuleScale.class);
        manager.newItem("moduleSpeed", ItemModuleSpeed.class);
        manager.newItem("moduleTranslate", ItemModuleTranslate.class);
        manager.newItem("moduleSponge", ItemModuleSponge.class);
        manager.newItem("remoteController", RemoteController.class);
    }

    @Override
    public void loadEntities(ModManager manager)
    {
        GameRegistry.registerTileEntity(TileBiometricIdentifier.class, "biometricIdentifier");
        GameRegistry.registerTileEntity(TileCoercionDeriver.class, "coercionDeriver");
        GameRegistry.registerTileEntity(TileForceField.class, "forceField");
        GameRegistry.registerTileEntity(TileForceFieldProjector.class, "forceFieldProjector");
        GameRegistry.registerTileEntity(TileFortronCapacitor.class, "fortronCapacitor");
        GameRegistry.registerTileEntity(TileInterdictionMatrix.class, "interdictionMatrix");
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        channel = new SimpleNetworkWrapper(DOMAIN);

        MFFSSettings.load(getConfig());

        FluidRegistry.registerFluid(new Fortron());
        Fortron.FLUID_ID = FluidRegistry.getFluidID("fortron");

        MinecraftForge.EVENT_BUS.register(new ForgeSubscribeHandler());

        //TODO move to VoltzEngine packet system
        ModularForcefieldSystem.channel.registerMessage(EntityToggle.ServerHandler.class, EntityToggle.class, 0, Side.SERVER);
        channel.registerMessage(FortronSync.ClientHandler.class, FortronSync.class, 1, Side.CLIENT);
        ModularForcefieldSystem.channel.registerMessage(ChangeFrequency.ServerHandler.class, ChangeFrequency.class, 2, Side.SERVER);
        ModularForcefieldSystem.channel.registerMessage(ForcefieldCalculation.ClientHandler.class, ForcefieldCalculation.class, 3, Side.CLIENT);
        ModularForcefieldSystem.channel.registerMessage(BeamRequest.ClientHandler.class, BeamRequest.class, 4, Side.CLIENT);
        ModularForcefieldSystem.channel.registerMessage(ItemByteToggle.ServerHandler.class, ItemByteToggle.class, 5, Side.SERVER);
        ModularForcefieldSystem.channel.registerMessage(ItemStringToggle.ServerHandler.class, ItemStringToggle.class, 6, Side.SERVER);

        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
        proxy.postInit();
    }
}
