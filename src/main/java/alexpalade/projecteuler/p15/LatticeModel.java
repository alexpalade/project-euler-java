package alexpalade.projecteuler.p15;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class LatticeModel {

    int n;
    int currentStepIndex;
    List<Step> steps;
    HashMap<Step, Long> scores;

    LatticeModel(int n) {
        this.n = n;
        
        currentStepIndex = 0;
        
        scores = new HashMap<>();
        scores.put(new Step(n-1, n-1), 1L);
        
        steps = new ArrayList<>();
        LinkedList<Step> visitNext = new LinkedList<>();

        steps.add(new Step(n - 1, n - 1));
        visitNext.push(new Step(n - 1, n - 1));

        while (!visitNext.isEmpty()) {
            Step step = visitNext.remove();
            // LEFT
            if (step.i > 0) {
                Step next = new Step(step.i - 1, step.j);
                if (!steps.contains(next)) {
                    steps.add(next);
                    visitNext.add(next);
                }
            }
            // UP
            if (step.j > 0) {
                Step next = new Step(step.i, step.j - 1);
                if (!steps.contains(next)) {
                    steps.add(next);
                    visitNext.add(next);
                }
            }
        }
    }
    
    public long getScore(int i, int j) {
        Step s = new Step(i, j);
        if(scores.containsKey(s)) {
            return scores.get(s);
        }
        throw new RuntimeException("Could not find score for node " + i + ", " + j);
    }
    
    public void solve() {
        while (currentStepIndex < n*n-1) {
            step();
        }
    }
    
    public void reset() {
        currentStepIndex = 0;
        scores.clear();
        scores.put(new Step(n-1, n-1), 1L);
    }

    public void step() {
        if (currentStepIndex ==  n*n-1) {return;}
        currentStepIndex ++;
        Step currentStep = steps.get(currentStepIndex);
        long currentScore = 0;
        if (currentStep.i + 1 < n) {
            Step parent1 = new Step(currentStep.i+1, currentStep.j);
            currentScore += scores.get(parent1);
        }
        if (currentStep.j + 1 < n) {
            Step parent1 = new Step(currentStep.i, currentStep.j+1);
            currentScore += scores.get(parent1);
        }
        scores.put(currentStep, currentScore);
    }
    
    public boolean nodeSolved(int i, int j) {
        Step s = new Step(i, j);
        
        return steps.contains(s) && steps.indexOf(s) <= currentStepIndex;
    }

    class Step {

        int i, j;

        Step(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof Step)) {
                return false;
            }
            return this.i == ((Step) other).i && this.j == ((Step) other).j;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 17 * hash + this.i;
            hash = 17 * hash + this.j;
            return hash;
        }
    }
}
