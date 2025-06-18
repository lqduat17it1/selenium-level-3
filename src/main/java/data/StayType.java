package data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StayType {
    OVERNIGHT("funnel-switcher", 0),
    DAY_USE("funnel-switcher", 1);

    private final String dataElementName;
    private final int dataHhBookingDurationType;
}
