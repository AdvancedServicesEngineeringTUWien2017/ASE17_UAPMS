package ase.shared.TspModels;

import java.util.List;

/**
 * Created by tommi on 05.06.17.
 */


public class TspResult {

    private TspResultStatus status;
    private List<TspLocation> tour;
    private double cost;

    public TspResultElasticAdaptionResult getQualityAdaptions() {
        return qualityAdaptions;
    }

    public void setQualityAdaptions(TspResultElasticAdaptionResult qualityAdaptions) {
        this.qualityAdaptions = qualityAdaptions;
    }

    public TspResultElasticAdaptionResult getPriorityAdaptions() {
        return priorityAdaptions;
    }

    public void setPriorityAdaptions(TspResultElasticAdaptionResult priorityAdaptions) {
        this.priorityAdaptions = priorityAdaptions;
    }

    private TspResultElasticAdaptionResult qualityAdaptions;
    private TspResultElasticAdaptionResult priorityAdaptions;

    public TspResultStatus getStatus() {
        return status;
    }

    public void setStatus(TspResultStatus status) {
        this.status = status;
    }

    public List<TspLocation> getTour() {
        return tour;
    }

    public void setTour(List<TspLocation> tour) {
        this.tour = tour;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }


    public static enum TspResultElasticAdaptionResult{
        CHANGED, ORIGINAL;
    }
}
