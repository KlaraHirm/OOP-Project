### /api/board
- **GET /**
    - Get all board
    - Return list of all boards
- **GET /{id}**
    - Gets a board
    - Returns the board
- **POST /{ID}**
    - adds cardlist to board with id = ID
    - Return saved cardlist
- POST /
    - Adds new board/ modifies board
    - Return saved board
- PUT /
    - Modifies metadata of the board (title, color, etc.)
    - Return saved board
- _PUT /{id}/reorder_
    - Moves a cardlist to a certain place
    - Parameters : carlist id, place
    - Return saved board
- **DELETE /{ID}**
    - Delete board with id
    - Return deleted board
- GET /websocket/{id}
    - Starts a websocket that notifies when this board changes

### /api/list
- GET /{ID}
    - Gets a list with id
- POST /{id}
    - Adds a card to a list with id
    - Body : card object to add
    - Return: saved card
- POST /
    - Overwrite all attributes of card list with id in card list json representation
    - Return: saved cardlist
- PUT /
    - Modifies metadata of the list (title, etc.)
    - Body : cardlist object to edit, with id
    - Return: saved cardlist
- PUT /reorder
    - Moves a card to a certain place
    - Parameters : originalList id, targetList id, card id, place
    - Return: saved cardlist
- DELETE /{id}
    - Deletes the list with id
    - Return deleted cardlist

### /api/card
- **GET /{id}**
    - Gets a single card
    - Returns the card
- **PUT /**
    - Modifies metadata of the card (title, other metadata)
    - Body : card object to edit, with id
    - Returns the saved card
- **DELETE /{id}**
    - Deletes a card
    - Returns the deleted card

### /api/tag
- _GET /{id}_
    - Gets a tag with id
- _POST /_
    - Adds a new tag
- _PUT /_
    - Modifies metadata of the tag
- _DELETE /{id}_
    - Deletes a tag


**Bold** : done

_Italics_ : not in basic requirements
