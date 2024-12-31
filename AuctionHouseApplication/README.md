# Auction House Application

### Landing Page

[Visit the application](http://deployedac.s3-website.us-east-2.amazonaws.com/)


---

Additionally, we have also include our package.json file in case there are issues with navigating through our app on AWS and you wish to try running it locally.

## Use-Cases

### Customer Use-Cases

- Search items
- Sort items
- View item

### Seller Use-Cases

- Create Account
- Close Account
- Login Account
- Add Item
- Remove Inactive Item
- Review Items
- Edit Item
- Publish Item
- Unpublish Item
- Archive Item
- Fulfill Item

### Buyer Use-Cases

- Open Account
- Close Account
- Login Account
- See Your Fund
- Add Funds
- Search Recently Sold
- Sort Recently Sold
- View Item
- Place Bid
- Review Active Bids
- Review Purchases
- Buy Now

### Admin Use-Cases

- Freeze/Unfreeze Items
- Generate Auction Report
- Generate Forensics Report

---

## How to Navigate Through the Application and Test This Iteration's Use-Cases

**IMPORTANT NOTES:**

1. Do NOT refresh/reload the application once there through the link as that will break the application's functionality.
2. When accessing certain pages on the application such as "view active items", "review items", other similar pages, or when viewing a specific item, items past their bid or purchase end date will automatically become completed or failed depending on the conditions described on the "FINALIZED" version of the use-cases before any of the items and their contents show up.
3. Despite what is said on the README regarding each use-case, it is important to take into consideration what is described on the "FINALIZED" version of the use-cases as to why the condition behaves in certain ways when certain actions are taken by the user.
4. You will be treated as a Customer until you log in as either a seller or buyer. If you log out of your account at any point, you will be treated as a Customer again until you log back in as either a seller or buyer.
5. Do NOT use the username "Admin1" when creating an account as that username is already taken
6. To log in as the auction house's "Admin" side, use the username "Admin1" and password "Admin1", both without the quotes and while ensuring the correct capitalization.
7. On the "Reports" page on the Admin, the "Total Funds:" section represents the required generated auction report, and the "auction forensics" section represents the required generated forensics report.
8. While we don't expect there to be any, to avoid any unforeseen issues, we recommend running our application inside of an incognito window of your browser.

### Customer Use-Cases

#### View Item

1. On the landing page, you are considered a "Customer" until you log into an account.
2. Click on the "View Items" button on the top left side of the navbar.
3. You will see all the published items displayed as rectangular cards in a grid format.
4. Each item card contains information such as name, description, images, published date, expiration date, initial price, and the current highest bid if there is one.

#### Sort Items

1. While viewing items, you can sort them by initial price, published date, or expiration date.
2. Use the "Sort by" dropdown and select your sorting option.
3. Choose between "Ascending" or "Descending" order.
4. Items are automatically sorted without needing additional buttons.

#### Search Items

1. Use the search bar on the top left side of the page (placeholder: "search by name or description").
2. As you type, only items containing the typed string in their name or description will appear.
3. To undo the search, clear the search bar.
4. In addition to this, you can also search items by price range, and when that happens, only items with a price within the typed range will be shown.

### Seller Use-Cases

#### Create Account

1. Click on the "Create Account" button at the top-right corner of the page.
2. Fill in the username, password, and select "Seller" as the user type.
3. Avoid using username "Admin1" s that username is already taken
4. Upon successful account creation, you will see a message: "Account created successfully!".
5. You will NOT be able to successfully create your account if the username you chose already exists as either a seller or a buyer or the admin.

#### Login Account

1. Click on the "Login" button next to the "Create Account" button.
2. Enter your username and password.
3. Click the "Login" button.
4. If credentials are incorrect, you will see: "Invalid username or password.".

#### Add Item

1. After logging in, click the "Add Item" button on the top left side of the screen.
2. Fill in the fields: "Item Name", "Initial Price", "Item Description", "Bid End Date", and upload images, and toggle the "Is Buy Now" option if you wish to create your item as of type "Buy Now" instead of "Bidding".
3. Click the "Add Item" button to add your item and redirect to the Review Items Page.

#### Review Items

1. On the Review Items Page, you will see all your added items.
2. Use the dropdown to filter items by activity status.
3. Click on an item card to view more details on the Review Specific Item page.

#### Remove Inactive Item

1. Click the "Remove Item" button on an inactive item to delete it.

#### Publish Item

1. Click the "Publish Item" button on an inactive item to make it active.
2. Ensure the BidEndDate is later than the current date and time.
3. If active items have associated bids or have been bought, a table with the related information will be shown.
4. You will only be able to publish an item if the bid or purchase end date selected is at least 30 minutes from the current time.

#### Unpublish Item

1. Click the "Unpublish Item" button on an active item to make it inactive.
2. This button is only available if there are no bids on the item.

#### Edit Item

1. Click the "Edit Details" button on an inactive item to edit any of its details.
2. Update the fields and click "Save Edits".

#### Close Account

1. Click the "Close Account" button on the top right side of the navbar.
2. Confirm the action in the alert.
3. If you have active items, you will see: "Cannot close account with active auctions".
4. If successful, you will see: "Account closed successfully!".

#### Archive Item

1. After logging in as a Seller, the user should navigate to the Review Items page.
2. On the Review Items page, you have the option to select the "Inactive" or "Failed" filter options from the Activity Status filter options
   if you do not feel like trying out all the items one by one.
3. Click on an "Inactive" or "Failed" item which will take you to the /review-items/specific-items for that particular item.
4. Scroll down to the bottom of the /review-items/specific-item page and click on the "Archive Item" button.
5. If successful, you will briefly see a green alert notification stating "Item has been successfully archived." You will also see that the "Activity Status" field has now been updated to "Archived".

#### Fulfill Item

1. After logging in as a Seller, the user should navigate to the Review Items page.
2. On the Review Items page, you have the option to select the "Completed" filter options from the Activity Status filter options
   if you do not feel like trying out all the items one by one.
3. Click on a "Completed" item which will take you to the /review-items/specific-items for that particular item.
4. Scroll down to the bottom of the /review-items/specific-item page and click on the "Fulfill Item" button.
5. If successful, you will briefly see a green alert notification stating "Item has been successfully fulfilled." You will also see that the "Activity Status" field has now been updated to "Archived".
6. After fulfilling an item, the seller's funds, the winning buyer's funds, and the Admin's (or Auction House's) funds should be updated accordingly. 95% of the fulfillment price of the item will go to the seller and the remaining 5% will go to the Admin (or Auction House). Of course, if the item was of type "Buy Now" then the winning buyer is just the person who bought the item.
7. If completed or archived items have associated bids or have been bought, a table with the related information will be shown.

#### Request Unfreeze Item

1. First, click on a "Frozen" item from the "Review Items" page
2. Click on the "Request Unfreeze" button
3. Once that is done, the seller will be given a message saying that they need to wait for the Admin to unfreeze the item
4. If an item is frozen past its bid or purchase end date, its activity status will become "Failed"

### Buyer Use-Cases

#### Open Account

1. Click on the "Create Account" button at the top-right corner of the page.
2. Fill in the username, password, and select "Buyer" as the user type.
3. Avoid using username "Admin1" s that username is already taken
4. Upon successful account creation, you will see a message: "Account created successfully!".
5. You will NOT be able to successfully create your account if the username you chose already exists as either a seller or a buyer or the admin.

#### Login Account

1. Click on the "Login" button next to the "Create Account" button.
2. Enter your username and password.
3. Click the "Login" button.
4. If credentials are incorrect, you will see: "Invalid username or password.".

#### See Your Fund

1. After logging in, click the "See Your Funds" button on the top right-side the screen.
2. Your fund balance will be displayed as a pop-up message, e.g., "Your funds: $...".
3. Click the "OK" button to close the message.

#### Add Funds

1. After logging in, click the "Add Funds" button on the top-left side of the screen.
2. You will be redirected to the Add Funds page.
3. On this page, your current balance will be displayed as: "Current Balance: $...".
4. Enter the amount you want to add in the "Enter amount to add" box and click the "Add Funds" button to update your balance.
5. The "Funds added successfully!" message will be shown.
6. The updated balance will be shown as "Current Balance: $...". You can also click the "See Your Funds" button in the navbar to view the updated balance.
7. Make sure the entered value is an integer; decimal values are not allowed.

#### Close Account

1. Click the "Close Account" button on the top right side of the navbar.
2. Confirm the action in the alert.
3. If you have active bids, you will see: "Cannot close account with active auctions" and the account will not be closed.
4. If successful, you will see: "Account closed successfully!" and the account will be closed.

#### Search Recently Sold

1. Click on "View Recently Sold Items" to be shown a list of items that have been sold in the past 24 hours.
2. You can click on the "View Details" button to see more details about the item.
3. Search recently sold items works in a similar manner as it does for "View Active Items".

#### Sort Recently Sold

1. On the "View Recently Sold Items" page, sorting can be done in a similar manner as done on the "View Active Items" page.

#### View Item

1. Buyer can view all the active items by clicking on "View Items" on the top left side of the navbar.
2. Buyer can initially see different details of the item like name, description, images, published date, expiration date, initial price, and the current highest bid if there is one.
3. Buyer can click on the "View Details" for each item to view more details of that.
4. Once that is done, Buyer can see the additional details of the item like name, description, images, published date, expiration date, initial price, and bids (if any).
5. Buyer can see the bid history of the item.
6. Buyer can see the highest bid on the item.
7. Buyer can see the time remaining for the bid to end.

#### Place bid

1. Buyer can place a bid on the item.
2. To place a custom bid, enter the desired amount and click the "Place Custom Entered Bid" button.
3. Make sure the entered bid is an integer; decimal values are not allowed.
4. To place the next higher bid (by $1), simply click the "Place Next Highest Bid (The Item's Current Highest Bid + $1)" button.
5. In order to leave the view-specific-item page, click on the "View Active Items" or any other option on the Navbar.
6. If the item currently has no bids, the item's price will be treated as the item's current highest bid, the buyer's highest bid will be treated as 0, and the difference between the buyer's requested bid and their highest bid will just be treated as
   the buyer's requested bid when determining if the buyer can place their desired bid.
7. Buyer can only place a bid if their requested bid is higher than the item's current highest bid, if they are not related to
   the item's overall highest bid, and if the difference between the buyer's requested bid and their highest bid + the sum of their current highest bids on active items + the sum of their current highest bids on completed but not frozen items is less than or equal to their current funds.
8. At the bottom of the page used to place bids for an item, a table with information related to all of bids placed for that item will be shown to the user
9. Buyer will NOT be able to place a bid(s) on an active item that is currently frozen

#### Buy Now

1. After logging in as a buyer, go to the "View Items" page.
2. Find an item that has a "Type:" field with a value of "Buy Now" and click on the "View Details" button to be taken to the /view-items/view-specific-item page for that item.
3. Click on the "Buy Now" button to buy the item.
4. Upon successfully buying the item, there will be a green alert notification that states "You have successfully purchased the item!".
5. If you scroll down to the "Purchase History" table, you will also see the purchase that you have just made on this item.
6. In order to leave the view-specific-item page, click on the "View Active Items" or any other option on the Navbar.
7. Buyer can only buy an item if the item is not already bought, and if the difference between the buyer's requested bid and their highest bid + the sum of their current highest bids on active items + the sum of their current highest bids on completed but not frozen items is less than or equal to their current funds.
8. If a buyer successfully buys an item, a table with the purchase will be shown at the bottom of the page used to buy an item, and that item's activity status will immediately become completed.
9. Buyer will NOT be able to buy an active item that is currently frozen

#### Review Active Bids

1. Click on "Review Active Bids" to be shown a list fo all Active items that the buyer has a bid on.

#### Review Purchases

1. Click on "Review Purchases" to be shown a list of items where Buyer was the successful bidder on or the first to purchase an item if it was of type "Buy Now", and the item has already been fulfilled.

### Admin Use-Cases

#### Freeze/Unfreeze Item

1. After logging in, click the "View Active Items" button on the Navbar.
2. A list of all active items will be displayed.
3. To see more details about an item, click the "View Details" button next to the item.
4. To manage an item’s frozen status, click the "Freeze" or "Unfreeze" button corresponding to the item.
   - When you freeze an item, a pop-up message will appear: "Item successfully frozen!"
   - When you unfreeze an item, a pop-up message will appear: "Item successfully unfrozen!"
5. To review unfreeze requests, click the "Unfreeze Requests" button in the Navbar.
6. A table displaying all unfreeze requests for active items will appear.
7. From this table, click "Unfreeze" to unfreeze the item or "Deny" to reject the request.
8. When you deny a request, a pop-up message will appear: "Unfreeze request denied", and the seller will be able to request to unfreeze that item
9. When you "unfreeze" a request, a pop-up message will appear to inform that the item has been unfrozen

#### Generate Auction Report and Generate Forensics Report

1. Log in in as Admin.
2. Admin will click on the "Reports" tab on the navbar.
3. Once there, they can both generate the Auction and Forensics reports as described under the "IMPORTANT NOTES" section of this README..

---

---

## Getting Started

First, run the development server:

```bash
npm run dev
# or
yarn dev
# or
pnpm dev
# or
bun dev
```
