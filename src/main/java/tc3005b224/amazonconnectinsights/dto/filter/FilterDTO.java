package tc3005b224.amazonconnectinsights.dto.filter;

import java.util.List;

public class FilterDTO {
    private String filterKey;
    private List<String> filterValues;

    public FilterDTO() {
        ;
    }

    public FilterDTO(String filterKey, List<String> filterValues) {
        this.filterKey = filterKey;
        this.filterValues = filterValues;
    }

    public String getFilterKey() {
        return filterKey;
    }

    public void setFilterKey(String filterKey) {
        this.filterKey = filterKey;
    }

    public List<String> getFilterValues() {
        return filterValues;
    }

    public void setFilterValues(List<String> filterValues) {
        this.filterValues = filterValues;
    }

    @Override
    public String toString() {
        return "FilterDTO{" +
                "filterKey='" + filterKey + '\'' +
                ", filterValues=" + filterValues +
                '}';
    }
}
