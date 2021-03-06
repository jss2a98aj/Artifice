package shukaro.artifice.block.decorative;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import shukaro.artifice.ArtificeBlocks;
import shukaro.artifice.ArtificeCore;
import shukaro.artifice.render.TextureHandler;

import java.util.List;
import java.util.Random;

public class BlockRockSlab extends BlockSlab
{
    private static String[] types = {"brick", "cobble", "paver", "antipaver"};

    private IIcon paverSide;
    private IIcon[] icons = new IIcon[types.length];

    private boolean isDouble;
    private String name;
    private int color = 16777215;

    public BlockRockSlab(String name, boolean isDouble)
    {
        super(isDouble, Material.rock);
        setCreativeTab(ArtificeCore.worldTab);
        setLightOpacity(0);
        setHardness(1.5F);
        setResistance(10.0F);
        setBlockName("artifice." + name + (isDouble ? ".doubleslab" : ".slab"));
        this.name = name;
        this.isDouble = isDouble;
    }

    public BlockRockSlab(String name, int color, boolean isDouble)
    {
        this(name, isDouble);
        this.color = color;
    }

    @Override
    public Item getItemDropped(int meta, Random rand, int fortune)
    {
        if (isDouble)
        {
            if (this == ArtificeBlocks.blockBasaltDoubleSlab)
                return Item.getItemFromBlock(ArtificeBlocks.blockBasaltSlab);
            else if (this == ArtificeBlocks.blockMarbleDoubleSlab)
                return Item.getItemFromBlock(ArtificeBlocks.blockMarbleSlab);
            else
            {
                int i = 0;
                for (Block b : ArtificeBlocks.blockLimestoneDoubleSlabs)
                {
                    if (this == b)
                        return Item.getItemFromBlock(ArtificeBlocks.blockLimestoneSlabs[i]);
                    i++;
                }
            }
        }
        return Item.getItemFromBlock(this);
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int x, int y, int z)
    {
        return this.getItemDropped(world.getBlockMetadata(x, y, z), world.rand, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item id, CreativeTabs tab, List list)
    {
        if (!isDouble)
        {
            for (int i = 0; i < types.length; i++)
                list.add(new ItemStack(id, 1, i));
        }
    }

    @Override
    public String func_150002_b(int meta)
    {
        meta = meta > 7 ? meta - 8 : meta;
        if (meta >= types.length)
            meta = 0;
        return "tile.artifice." + name + (isDouble ? ".doubleslab." : ".slab.") + types[meta];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg)
    {
        paverSide = TextureHandler.registerIcon(reg, "paverside", name.split("[.]")[0]);
        for (int i=0; i<ArtificeBlocks.rockBlocks.length; i++)
        {
            if (ArtificeBlocks.rockBlocks[i].getUnlocalizedName().contains(name))
            {
                TextureHandler.registerConnectedTexture(reg, this, 2, 0, "paver", name.split("[.]")[0]);
                TextureHandler.registerConnectedTexture(reg, this, 2, 1, "paver", name.split("[.]")[0]);
                icons[2] = TextureHandler.getConnectedTexture(this, 2, 0).icon;
                TextureHandler.registerConnectedTexture(reg, this, 3, 0, "antipaver", name.split("[.]")[0]);
                TextureHandler.registerConnectedTexture(reg, this, 3, 1, "antipaver", name.split("[.]")[0]);
                icons[3] = TextureHandler.getConnectedTexture(this, 3, 0).icon;
                break;
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        meta = meta & 7;
        if (meta > types.length)
            meta = 0;
        if (meta == 2 || meta == 3)
        {
            if (side == 0 || side == 1)
            {
                return this.icons[meta];
            }
            else
                return this.paverSide;
        }
        if (meta == 0)
            meta = 2;
        if (name.split("[.]")[0].equals("basalt"))
            return ArtificeBlocks.blockBasalt.getIcon(side, meta);
        else if (name.split("[.]")[0].equals("marble"))
            return ArtificeBlocks.blockMarble.getIcon(side, meta);
        else
            return ArtificeBlocks.blockLimestones[0].getIcon(side, meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess block, int x, int y, int z, int side)
    {
        int meta = block.getBlockMetadata(x, y, z) & 7;
        return this.getIcon(side, meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta)
    {
        return color;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess block, int x, int y, int z)
    {
        return color;
    }
}
