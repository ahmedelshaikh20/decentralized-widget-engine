package org.example.widgetprocessor.service.widgetprocessor;

import org.example.widgetprocessor.model.userevent.UserEvent;
import org.springframework.stereotype.Service;

@Service
public class EventProcessor {

    public void processEvent(UserEvent event) {

        // TODO: Add the logic of handling each event here
        switch (event.getEventType()) {
            case FLIGHT_SEARCH:
                handleFlightSearch(event);
                break;

            case FLIGHT_BOOKED:
                handleFlightBooked(event);
                break;

            case INSURANCE_QUOTE:
                handleInsuranceQuote(event);
                break;

            case INSURANCE_COMPLETED:
                handleInsuranceCompleted(event);
                break;

            case FURNITURE_BROWSE:
                handleFurnitureBrowse(event);
                break;

            case FURNITURE_FAVORITE:
                handleFurnitureFavorite(event);
                break;

            case CONTRACT_SIGNED:
                handleContractSigned(event);
                break;

            case HOME_VISIT:
                handleHomeVisit(event);
                break;

            case TEST_EVENT:
            default:
             //   log.debug("Received TEST_EVENT or unsupported event type.");
        }
    }



    private void handleFlightSearch(UserEvent event) {
    }

    private void handleFlightBooked(UserEvent event) {
    }

    private void handleInsuranceQuote(UserEvent event) {
    }

    private void handleInsuranceCompleted(UserEvent event) {
    }

    private void handleFurnitureBrowse(UserEvent event) {
    }

    private void handleFurnitureFavorite(UserEvent event) {
    }

    private void handleContractSigned(UserEvent event) {
    }

    private void handleHomeVisit(UserEvent event) {
    }
}
