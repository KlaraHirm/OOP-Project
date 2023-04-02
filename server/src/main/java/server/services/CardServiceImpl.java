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
     * @param cardId
     * @return
     */
    @Override
    public Card getCard(long cardId) {
        if (!cardRepo.existsById(cardId)) return null;

        return cardRepo.findById(cardId).get();
    }

    /**
     * @param card
     * @return
     */
    @Override
    public Card editCard(Card card) {
        if (card == null) return null;

        if (!cardRepo.existsById(card.id)) return null;

        return cardRepo.save(card);
    }

    /**
     * @param cardId
     * @return
     */
    @Override
    public Card deleteCard(long cardId) {
        if (!cardRepo.existsById(cardId)) return null;

        Card deleted = cardRepo.findById(cardId).get();
        cardRepo.deleteById(cardId);

        return deleted;
    }
}
