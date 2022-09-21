package model;

import java.util.ArrayList;
import java.util.List;

public abstract class OptimiserImpl implements Optimiser {
	private List<Optimiser> optimisers;
	private boolean multiplePasses;
	
	public OptimiserImpl(boolean multiplePasses, int optimisations) {
		this.multiplePasses = multiplePasses;
		optimisers = new ArrayList<>();
		List<Class<? extends Optimiser>> optimisers = getOptimisers();
		for (int i=0, pow; i<optimisers.size(); i++) {
			pow = 1 << i;
			if ((optimisations & pow) != 0) {
				try {
					this.optimisers.add(optimisers.get(i).getDeclaredConstructor().newInstance());
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
	}
	
	public abstract List<Class<? extends Optimiser>> getOptimisers();
	
	@Override
	public AST visit(AST ast) {
		AST temp = ast;
		int hashcode;
		do {
			hashcode = temp.hashCode();
			for (Optimiser optimiser : optimisers)
				temp = optimiser.apply(temp);
		} while (multiplePasses && temp.hashCode() != hashcode);
		return temp;
	}
}
