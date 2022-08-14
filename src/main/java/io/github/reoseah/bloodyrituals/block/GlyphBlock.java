package io.github.reoseah.bloodyrituals.block;

import io.github.reoseah.bloodyrituals.BloodyRituals;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class GlyphBlock extends Block {
    public static final Property<Direction> TYPE = EnumProperty.of("type", Direction.class, dir -> dir.getAxis().isHorizontal());

    protected static final VoxelShape SHAPE = Block.createCuboidShape(2, 0, 2, 14, 1, 14);

    public GlyphBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TYPE);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        for (Direction direction : Direction.Type.HORIZONTAL) {
            if (!ctx.getWorld().getBlockState(ctx.getBlockPos().offset(direction)).isOf(this)) {
                return this.getDefaultState().with(TYPE, ctx.getPlayerFacing());
            }
        }
        return BloodyRituals.Blocks.CENTER_GLYPH.getDefaultState();
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return Block.isFaceFullSquare(world.getBlockState(pos.down()).getSidesShape(world, pos.down()), Direction.UP) || Block.isFaceFullSquare(world.getBlockState(pos.down()).getCollisionShape(world, pos.down()), Direction.UP);
    }
}
