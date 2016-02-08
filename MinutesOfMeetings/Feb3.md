# Minutes of Meeting 
## Feb 3, 2015

- Refactor likely required for sidebar
- Several activities will need to be refactored to be `Fragment` subclasses.
- MainActivity will be responsible for sidebar presentation and logic
- Note that "detail" screens should still be Activities.

- On Inventory page, cells should have 2 ways to updated quantity:
	- Type in new quantity
	- "Increment" and "Decrement" buttons.
		- These buttons should have a default update value of 1.
		- On the detail page of the item, the update amount should be able to be updated. This new amount should stay around and be saved to Parse.x

		