package dev.su5ed.mffs.util;

import dev.su5ed.mffs.api.module.Module;
import dev.su5ed.mffs.blockentity.ProjectorBlockEntity;
import net.minecraft.core.BlockPos;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * A thread that allows multi-threading calculation of projector fields.
 *
 * @author Calclavia
 */
public class ProjectorCalculationThread extends Thread {
    public interface ThreadCallBack {
        /**
         * Called when the thread finishes the calculation.
         */
        void onThreadComplete();
    }

    private final ProjectorBlockEntity projector;
    @Nullable
    private final ThreadCallBack callBack;

    public ProjectorCalculationThread(ProjectorBlockEntity projector) {
        this(projector, null);
    }

    public ProjectorCalculationThread(ProjectorBlockEntity projector, @Nullable ThreadCallBack callBack) {
		this.projector = projector;
        this.callBack = callBack;
    }

    @Override
    public void run() {
        this.projector.isCalculating = true;

        try {
            Set<BlockPos> exteriorPoints = this.projector.getMode().getExteriorPoints(this.projector);

            BlockPos translation = this.projector.getTranslation();
            int rotationYaw = this.projector.getRotationYaw();
            int rotationPitch = this.projector.getRotationPitch();
			
			StreamEx.of(exteriorPoints)
				.map(pos -> rotationYaw != 0 || rotationPitch != 0 ? CalcUtil.rotateByAngle(pos, rotationYaw, rotationPitch) : pos)
				.map(pos -> pos.offset(this.projector.getBlockPos()).offset(translation))
				.forEach(pos -> {
					if (pos.getY() <= this.projector.getLevel().getHeight()) {
						this.projector.getCalculatedField().add(pos);
					}
				});

            for (Module module : this.projector.getModules()) {
                module.onCalculate(this.projector, this.projector.getCalculatedField());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.projector.isCalculating = false;
        this.projector.isCalculated = true;

        if (this.callBack != null) {
            this.callBack.onThreadComplete();
        }
    }
}