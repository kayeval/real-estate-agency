package main.model.User.Customer;

import main.model.Property.DeactivatedPropertyException;
import main.model.Property.Property;
import main.model.Property.RentalProperty;
import main.model.Property.SoldPropertyException;
import main.model.Proposal.*;
import main.model.User.PropertyOwner.Landlord;
import main.model.User.PropertyOwner.PropertyOwner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RenterTest {
    private Customer renter;
    private PropertyOwner propertyOwner;
    private Proposal proposal;
    private LocalDate date;
    private Property property;
    private Set<ContractDuration> acceptableDurations;

    @BeforeEach
    public void setUp() throws Exception {
        acceptableDurations = new HashSet<>();
        acceptableDurations.add(ContractDuration.ONE_YEAR);
        acceptableDurations.add(ContractDuration.TWO_YEARS);

        propertyOwner = new Landlord("Test", "test@test.com");
        renter = new Renter("Test", "test@a.com", 12000.0, "Doctor");
        property = new RentalProperty("123 A St", "Melbourne", null, null,
                10000, propertyOwner, acceptableDurations);
        date = LocalDate.now(ZoneId.systemDefault());
    }

    @Test
    public void itShouldThrowInvalidContractDurationExceptionWhenSubmittingProposalWithUnacceptableContractDuration() {
        proposal = new Application( 11000, property, renter, ContractDuration.SIX_MONTHS);

        InvalidContractDurationException ex = assertThrows(InvalidContractDurationException.class, () ->
                renter.submitProposal(proposal));
        assertEquals("This contract duration is not among the choices given by the landlord.", ex.getMessage());
    }

    @Test
    public void submitApplicationTest() {
        proposal = new Application(11000, property, renter, ContractDuration.ONE_YEAR);
        try {
            renter.submitProposal(proposal);
        } catch (DeactivatedPropertyException | InvalidContractDurationException | SoldPropertyException | ProposalNotFoundException | PendingProposalException e) {
            e.printStackTrace();
        }

        assertEquals(proposal.getProposalID(), proposal.getProperty().getProposal().getProposalID());
    }
}
