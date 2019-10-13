package main.model.User.Employee.SalesPerson;

import main.model.Property.DeactivatedPropertyException;
import main.model.Property.Property;
import main.model.Property.SaleProperty;
import main.model.Property.SoldPropertyException;
import main.model.Proposal.*;
import main.model.User.Customer.Buyer;
import main.model.User.Customer.Customer;
import main.model.User.PropertyOwner.NotListedPropertyException;
import main.model.User.PropertyOwner.PropertyOwner;
import main.model.User.PropertyOwner.Vendor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SalesPersonTest {
    private Customer buyer;
    private SalesPerson salesPerson;
    private PropertyOwner propertyOwner;
    private PropertyOwner propertyOwner2;
    private Proposal proposal;
    private LocalDateTime date;
    private Property property;

    @BeforeEach
    public void setUp() throws Exception {
        propertyOwner = new Vendor("Test", "test@test.com");
        propertyOwner2 = new Vendor("Test1", "test@test.com");
        buyer = new Buyer("Test", "test@a.com");
        property = new SaleProperty("123 A St", "Melbourne", null, null, 10000, propertyOwner);
        date = LocalDateTime.now(ZoneId.systemDefault()).minus(6, ChronoUnit.DAYS);
        salesPerson = new SalesConsultant("test", "test@test.com", date.toLocalDate(), 50040);
        proposal = new Offer( 10000, property, buyer);
    }

    @Test
    public void itShouldThrowExpiredProposalExceptionWhenAcceptingExpiredProposal() {
        proposal.setSubmissionDate(date);
        try {
            buyer.submitProposal(proposal);
        } catch (DeactivatedPropertyException | SoldPropertyException | ProposalNotFoundException | InvalidContractDurationException | PendingProposalException e) {
            e.printStackTrace();
        }

        ExpiredProposalException ex = assertThrows(ExpiredProposalException.class, () -> salesPerson.acceptProposal(proposal));
        assertEquals("This offer or application has already expired.", ex.getMessage());
    }

    @Test
    public void itShouldThrowNotListedPropertyExceptionWhenAcceptingOfferForUnlistedProperty() {
        proposal = new Offer( 10000, property, buyer);

        try {
            buyer.submitProposal(proposal);
        } catch (DeactivatedPropertyException | SoldPropertyException | InvalidContractDurationException | ProposalNotFoundException | PendingProposalException e) {
            e.printStackTrace();
        }

        NotListedPropertyException ex = assertThrows(NotListedPropertyException.class, () -> salesPerson.acceptProposal(proposal));
        assertEquals("This property does not belong to the property owner.", ex.getMessage());
    }
}
