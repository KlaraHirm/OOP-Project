### Feedback on Backlog Draft for Group 13

### Submission

*Mark: Pass*
The submission consists of a single PDF file, uploaded in the correct directory. There is only the slight problem of the extra underscore. 
**To ensure there are no problems with your submission, make sure that the uploaded name is exactly the same as in the assignment description for the final version.**

### Backlog Structure

*Mark: Pass*
The document has the correct structure (list of stakeholders, terminology, list of user stories). However, not all of these parts are covered with a sufficient level of detail.

### Epics

*Mark: Insufficient.*
While user stories are sorted by priority using the MoSCoW method (following the format from last year's backlog), this assignment required the use of epics, as described in the Requirements Engineering lecture and the [assignment description](https://se.ewi.tudelft.nl/oopp/assignments/backlog/#epics).
For the final version of the Backlog, please make sure that the following points are met:
- The first epic is the Minimal App (minimal viable product);
- The subsequent epics correspond to features;
- Every epic has a clarifying description;
- The epics form a complete and prioritised representation of all of the features for this project.

Additionally (and optionally), consider using mock-ups for epics. These will aid both the reader of the backlog in better understanding the descriptions of the epics, and you in ensuring that there are no overlaps or gaps.

### User Stories

*Mark: Sufficient*
The **format** of the user stories is incomplete. Recall from the Requirement Engineering lecture, example backlog and the [assignment description](https://se.ewi.tudelft.nl/oopp/assignments/backlog/#user-stories-1) that user stories should also contain reasons / goals / motivation - why is this feature important? How does it benefit the stakeholder?

The focus is placed on the **user perspective**. However, note that two different types of users are identified in the Requirements section, but only one is described in the Stakeholders section.

Stories describe one particular interaction/workflow and have little to no overlap.

Remarks about ambiguous phrasings:
- "*create a card with title in a list*" - can this title be changed?
- "*delete lists*" - what happens to the cards inside the lists?
- "*allow others to access the board*" - auto-synchronisation, which is a *mandatory* requirement, is not mentioned.
- "*see the board and be able to make modifications to it*" - what kind of modifications?
- Please include all of the features as described in the lectures in the backlog. You may choose later not to implement them, but they should still be listed.
- "*add descriptions to cards*" - how and when can users do this?
- users with / without passwords is not a distinction that corresponds to the real-life domain, instead it is related to features which can be implemented. Your user stories should reflect this.
- "*restart the server*" - how does this reflect in the state of the application? Are boards / lists / cards lost? Please refer to the project description.
- "*create a board*" - can this be repeated (multiple boards) or do users only have one, default board?
- "*share the url (or other form of identification)*" - what form of identification? Either ask the client or make a decision as a group.

### Acceptance Criteria

*Mark: Good*
- *Conciseness:* The size of most user stories is small and their effect is clear, so their acceptance criteria are implicit.
- *Clarity:* Some of the user stories do not make it clear when they have been finished:
"*see the board and be able to make modifications to it*", "*to restart the server*", "*filter a list or board by tag*".