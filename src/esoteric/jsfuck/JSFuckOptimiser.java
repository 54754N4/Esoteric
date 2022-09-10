package esoteric.jsfuck;

import java.util.Arrays;
import java.util.List;

import esoteric.jsfuck.optimiser.JSPrimitivesMerger;
import model.Optimiser;
import model.OptimiserImpl;

public class JSFuckOptimiser extends OptimiserImpl {
	public static final int 
		PM = 1 << 0;	// primitives merger
	public static final int OPTIMISATIONS = 1;
	public static final List<Class<? extends Optimiser>> OPTIMISERS = Arrays.asList(
			JSPrimitivesMerger.class
	);
	
	public JSFuckOptimiser() {
		this(PM);
	}
	
	public JSFuckOptimiser(int optimisations) {
		super(true, optimisations);
	}

	@Override
	public List<Class<? extends Optimiser>> getOptimisers() {
		return OPTIMISERS;
	}
}
