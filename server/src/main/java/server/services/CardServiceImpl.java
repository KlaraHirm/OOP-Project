package server.services;

import commons.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.CardRepository;
import server.services.interfaces.CardService;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepo;

    public CardServiceImpl(CardRepository cardRepo) {
        this.cardRepo = cardRepo;
    }


    /**
     * Get info about a card
     * @param cardId the id of the card
     * @return the card object
     * Returns null if the card does not exist
     */
    @Override
    public Card getCard(long cardId) {
        if (!cardRepo.existsById(cardId)) return null;

        return cardRepo.findById(cardId).get();
    }

    /**
     * Update a certain card
     * @param card the card object to edit, with the corresponding id
     * @return the edited card
     * Gives null if the card does not exist
     * Gives null if the body is malformed
     */
    @Override
    public Card editCard(Card card) {
        if (card == null) return null;

        if (!cardRepo.existsById(card.id)) return null;

        return cardRepo.save(card);
    }

    /**
     * Delete a card
     * @param cardId the id of the card to delete
     * @return the whole board as updated
     * Returns nukk if the card, list or board do not exist
     */
    @Override
    public Card deleteCard(long cardId) {
        if (!cardRepo.existsById(cardId)) return null;

        Card deleted = cardRepo.findById(cardId).get();
        cardRepo.deleteById(cardId);

        return deleted;
    }
}
