package esoteric.brainfuck;

import java.util.Arrays;
import java.util.List;

import esoteric.brainfuck.optimiser.BFOptimiserLoopConverter;
import esoteric.brainfuck.optimiser.BFOptimiserOffsetFuser;
import esoteric.brainfuck.optimiser.BFOptimiserRLE;
import esoteric.brainfuck.optimiser.BFOptimiserSegmentation;
import model.Optimiser;
import model.OptimiserImpl;

public class BFOptimiser extends OptimiserImpl {
	public static final int 
		RLE = 1 << 0,	// run-length encoding optimisation 
		LC = 1 << 1, 	// loop conversion
		CS = 1 << 2, 	// code segmentation
		OF = 1 << 3;	// offset fuser
	public static final List<Class<? extends Optimiser>> OPTIMISERS = Arrays.asList(
			BFOptimiserRLE.class,
			BFOptimiserLoopConverter.class,
			BFOptimiserSegmentation.class,
			BFOptimiserOffsetFuser.class
	);
	
	public BFOptimiser() {
		this(RLE | LC | CS);
	}
	
	public BFOptimiser(int optimisations) {
		super(false, optimisations);
	}

	@Override
	public List<Class<? extends Optimiser>> getOptimisers() {
		return OPTIMISERS;
	}
}
