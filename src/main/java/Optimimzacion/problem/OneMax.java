package Optimimzacion.problem;

import org.uma.jmetal.problem.impl.AbstractBinaryProblem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.impl.DefaultBinarySolution;
import org.uma.jmetal.util.binarySet.BinarySet;

import java.util.BitSet;
import java.util.List;

public class OneMax extends AbstractBinaryProblem {

    private final int bits;

    public OneMax(int numberOfBits) {
        this.bits = numberOfBits;
        this.setNumberOfVariables(1);
        this.setNumberOfObjectives(1);
        this.setName("OneMax");
    }


    @Override
    protected int getBitsPerVariable(int i) {
        return 0;
    }


    public BinarySolution createSolution() {
        return new DefaultBinarySolution(this);
    }

    public void evaluate(BinarySolution solution) {
        int counterOnes = 0;
        BitSet bitset = (BitSet)solution.getVariableValue(0);

        for(int i = 0; i < bitset.length(); ++i) {
            if (bitset.get(i)) {
                ++counterOnes;
            }
        }

        solution.setObjective(0, (double)-1.0F * (double)counterOnes);
    }
}