package main.model.User.Customer;

import main.model.Property.DeactivatedPropertyException;
import main.model.Property.Property;
import main.model.Property.SaleProperty;
import main.model.Property.SoldPropertyException;
import main.model.Proposal.*;
import main.model.User.Employee.SalesPerson.SalesConsultant;
import main.model.User.Employee.SalesPerson.SalesPerson;
import main.model.User.InvalidEmailException;
import main.model.User.PropertyOwner.NotListedPropertyException;
import main.model.User.PropertyOwner.PropertyOwner;
import main.model.User.PropertyOwner.Vendor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {
    private Customer buyer, buyer2;
    private SalesPerson salesPerson;
    private PropertyOwner propertyOwner;
    private Proposal proposal, proposal2;
    private LocalDate date;
    private Property property;

    @BeforeEach
    public void setUp() throws Exception {
        date = LocalDate.now(ZoneId.systemDefault());
        salesPerson = new SalesConsultant("Test", "test@test.com", date, 55543);
        propertyOwner = new Vendor("Test", "test@test.com");
        buyer = new Buyer("Test", "test@a.com");
        buyer2 = new Buyer("Test 2", "test2@email.com");
        property = new SaleProperty("123 ABC Street", "Melbourne", null, null,
                10000, propertyOwner);
        proposal = new Offer(10000, property, buyer);
        proposal2 = new Offer(15000, property, buyer);
    }

    @Test
    public void submitProposalTest() {
        try {
            buyer.submitProposal(proposal);
        } catch (DeactivatedPropertyException | SoldPropertyException | InvalidContractDurationException |
                ProposalNotFoundException | PendingProposalException e) {
            e.printStackTrace();
        }

        try {
            assertTrue(proposal.getProperty().hasProposal(proposal.getProposalID()));
        } catch (ProposalNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void itShouldThrowDeactivatedPropertyExceptionWhenSubmittingProposalForDeactivatedProperty() {
        property.deactivate();

        DeactivatedPropertyException ex = assertThrows(DeactivatedPropertyException.class, () ->
                buyer.submitProposal(proposal));
        assertEquals("This property is inactive.", ex.getMessage());
    }

    @Test
    public void itShouldThrowDeactivatedPropertyExceptionWhenSubmittingProposalForSoldProperty() {
        submitProposalTest();

        try {
            salesPerson.acceptProposal(proposal);
        } catch (ExpiredProposalException | NotListedPropertyException | DeactivatedPropertyException | ProposalNotFoundException e) {
            e.printStackTrace();
        }

        proposal = new Offer(12000, property, buyer);

        DeactivatedPropertyException ex = assertThrows(DeactivatedPropertyException.class, () ->
                buyer.submitProposal(proposal));
        assertEquals("This property is inactive.", ex.getMessage());
    }

    @Test
    public void itShouldThrowPendingProposalExceptionWhenSubmittingProposalForPropertyWithPendingProposal() {
        submitProposalTest();

        try {
            buyer = new Buyer("Test 2", "test2@email.com");
        } catch (InvalidEmailException e) {
            e.printStackTrace();
        }

        PendingProposalException ex = assertThrows(PendingProposalException.class, () ->
                buyer.submitProposal(proposal2));
        assertEquals("Another proposal was submitted for this property.", ex.getMessage());
    }

    @Test
    public void rejectThenSubmitProposal() {
        submitProposalTest();

        try {
            salesPerson.rejectProposal(proposal);
        } catch (NotListedPropertyException | ProposalNotFoundException e) {
            e.printStackTrace();
        }

        try {
            buyer.submitProposal(proposal2);
        } catch (DeactivatedPropertyException | InvalidContractDurationException | SoldPropertyException |
                ProposalNotFoundException | PendingProposalException e) {
            e.printStackTrace();
        }

        assertEquals(proposal2.getProposalID(), proposal2.getProperty().getProposal().getProposalID());
    }
}
