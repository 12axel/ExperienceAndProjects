DROP TABLE Bid;
DROP TABLE Picture;
DROP TABLE Item;
DROP TABLE Admin;
DROP TABLE Buyer;
DROP TABLE Seller;

CREATE TABLE Seller(
Username VARCHAR(250) PRIMARY KEY,
Password VARCHAR(250),
Funds DECIMAL(20, 2) DEFAULT 0,
UserType VARCHAR(250),
IsClosed BOOLEAN
);

CREATE TABLE Buyer(
Username VARCHAR(250) PRIMARY KEY,
Password varchar(250),
AccountFunds DECIMAL(20, 2) DEFAULT 0,
UserType VARCHAR(250),
IsClosed BOOLEAN
);

CREATE TABLE Admin(
Username VARCHAR(250) PRIMARY KEY,
Password VARCHAR(250),
UserType VARCHAR(250),
TotalACFunds DECIMAL(20, 2)
);

CREATE TABLE Item(
ItemID INT PRIMARY KEY AUTO_INCREMENT,
Name VARCHAR(250),
Creator VARCHAR(250),
InitialPrice INT,
IsBuyNow BOOLEAN,
ItemDescription TEXT,
ActivityStatus VARCHAR(250) DEFAULT 'Inactive',
BidStartDate DATETIME DEFAULT NULL,
BidEndDate DATETIME,
PublishedDate DATETIME DEFAULT NULL,
SoldDate DATETIME DEFAULT NULL,
BuyerSoldTo VARCHAR(250) DEFAULT NULL,
IsFrozen BOOLEAN DEFAULT FALSE,
requestedUnfreeze BOOLEAN DEFAULT FALSE,
CONSTRAINT FK1_Item FOREIGN KEY(Creator) REFERENCES Seller (Username) ON DELETE CASCADE,
CONSTRAINT FK2_Item FOREIGN KEY(BuyerSoldTo) REFERENCES Buyer (Username) ON DELETE CASCADE
);

CREATE TABLE Picture(
PictureID INT PRIMARY KEY AUTO_INCREMENT,
RelatedItem INT,
URL TEXT,
CONSTRAINT FK_Picture FOREIGN KEY(RelatedItem) REFERENCES Item (ItemID) ON DELETE CASCADE
);

CREATE TABLE Bid(
BidID INT PRIMARY KEY AUTO_INCREMENT,
RelatedItemID INT,
AmountBid INT,
RelatedBuyer VARCHAR(250),
PlacementDate DATETIME,
CONSTRAINT FK1_Bid FOREIGN KEY(relatedItemID) REFERENCES Item (ItemID) ON DELETE CASCADE,
CONSTRAINT FK2_Bid FOREIGN KEY(relatedBuyer) REFERENCES Buyer (Username) ON DELETE CASCADE
);

SELECT * FROM Admin;

SELECT * FROM Seller;

SELECT * FROM Buyer;

DELETE FROM Bid;
DELETE FROM Picture;
DELETE FROM Item;
DELETE FROM Admin;
DELETE FROM Buyer;
DELETE FROM Seller;

INSERT INTO Admin VALUES('Admin1', 'Admin1', 'Admin', 0);

INSERT INTO Seller VALUES ('Test1', 'Test1', 100, 'Seller', FALSE);
UPDATE Item SET ActivityStatus = 'Inactive' WHERE ItemID = 1;

SELECT * FROM Item WHERE SYSDATE() > BidEndDate AND ItemID = 24;

SELECT * FROM Bid;

SELECT * FROM Item;

SELECT COUNT(*) FROM Item WHERE ActivityStatus != 'Inactive' AND BuyerSoldTo IS NULL;

DELETE FROM Seller WHERE Username = 'Admin1';

SELECT SYSDATE();

SELECT COUNT(*) FROM Picture;

INSERT INTO Picture (RelatedItem, URL) VALUES (3, 'Pic1');

SET SQL_SAFE_UPDATES = 0;

SELECT COUNT(*) FROM Item WHERE ActivityStatus = 'Archived';

DELETE FROM Item WHERE ItemID = 1;

DELETE FROM Bid;

SELECT SYSDATE();	

select NOW();

SET time_zone = 'America/New_York';
 
SELECT * FROM Seller;

SELECT * FROM Buyer;

DELETE FROM Bid WHERE BidID = 20;

DELETE FROM Buyer WHERE Username = 'Test1';

SELECT * FROM Item WHERE Creator = 'Seller2';

SELECT * FROM Bid;

ALTER TABLE Buyer ALTER AccountFunds SET DEFAULT 0;

ALTER TABLE Seller ALTER Funds SET DEFAULT 0;
ALTER TABLE Buyer CHANGE Funds AccountFunds INT;
ALTER TABLE Buyer ADD COLUMN FundsToBeWithdrawn INT AFTER AccountFunds;
ALTER TABLE Buyer DROP COLUMN FundsToBeWithdrawn;

SELECT IFNULL(SUM(MaxBidOnItem), 0) AS TotalActiveBids 
FROM (SELECT Item.ItemID, MAX(AmountBid) AS MaxBidOnItem FROM Item
JOIN Bid ON Bid.RelatedItemID = Item.ItemID
WHERE BidStartDate IS NOT NULL AND ActivityStatus = 'Active' AND SYSDATE() < BidEndDate AND RelatedBuyer = 'Buyer1'
GROUP BY Item.ItemID) AS GetItemsMaxBids;

SELECT * FROM Bid;

DELETE FROM Bid WHERE BidID = 15;

SELECT * FROM Buyer;


SELECT * FROM Seller;

INSERT INTO Buyer (Username, Password, AccountFunds, UserType, IsClosed) VALUES ('Buyer1', 'Buyer1', 1200, 'Buyer', FALSE);
INSERT INTO Buyer (Username, Password, AccountFunds, UserType, IsClosed) VALUES ('Buyer2', 'Buyer2', 20, 'Buyer', FALSE);
INSERT INTO Buyer (Username, Password, AccountFunds, UserType, IsClosed) VALUES ('Buyer3', 'Buyer3', 100, 'Buyer', FALSE);
INSERT INTO Buyer (Username, Password, AccountFunds, UserType, IsClosed) VALUES ('Buyer4', 'Buyer4', 800, 'Buyer', FALSE);
INSERT INTO Buyer (Username, Password, AccountFunds, UserType, IsClosed) VALUES ('Buyer5', 'Buyer5', 200, 'Buyer', FALSE);

INSERT INTO Bid (RelatedItemID, AmountBid, RelatedBuyer, PlacementDate) VALUES (2, 100, 'Buyer1', SYSDATE());

INSERT INTO Bid (RelatedItemID, AmountBid, RelatedBuyer, PlacementDate) VALUES (2, 50, 'Buyer1', SYSDATE());

INSERT INTO Bid (RelatedItemID, AmountBid, RelatedBuyer, PlacementDate) VALUES (3, 50, 'Buyer1', SYSDATE());

INSERT INTO Bid (RelatedItemID, AmountBid, RelatedBuyer, PlacementDate) VALUES (5, 50, 'Buyer1', DATE_SUB(SYSDATE(), INTERVAL 20 DAY_MINUTE));

INSERT INTO Bid (RelatedItemID, AmountBid, RelatedBuyer, PlacementDate) VALUES (2, 125, 'Buyer4', SYSDATE());

INSERT INTO Bid (RelatedItemID, AmountBid, RelatedBuyer, PlacementDate) VALUES (2, 300, 'Buyer2', SYSDATE());

UPDATE Item SET ActivityStatus = 'Active' WHERE ItemID = 1 OR ItemID = 2 OR ItemID = 3;

UPDATE Item SET BidStartDate = SYSDATE() WHERE ItemID = 1 OR ItemID = 2 OR ItemID = 3;

UPDATE Item SET BidStartDate = SYSDATE() WHERE ItemID = 5;

UPDATE Item SET BidEndDate = SYSDATE() WHERE ItemID = 5;

UPDATE Item SET ActivityStatus = 'Completed' WHERE ItemID = 5;

UPDATE Item
SET BidEndDate = DATE_ADD(SYSDATE(), INTERVAL 1 DAY)
WHERE ActivityStatus = 'Active';

UPDATE Item SET IsFrozen = TRUE WHERE ItemID = 3;

SELECT * FROM Picture;

UPDATE Seller SET Password = 'Special1' WHERE Username = 'Special';
 
UPDATE Seller SET isClosed = FALSE WHERE Username = 'Test2';
 
DELETE FROM Seller WHERE Username != 'Test1' AND Username != 'Test2';
 
DELETE FROM Seller WHERE Username = 'TestM';
 
UPDATE Item SET ActivityStatus = 'Inactive' WHERE ItemID = 76;

SELECT IFNULL(SUM(MaxBidOnItem), 0) FROM
(SELECT Item.ItemID, MAX(AmountBid) AS MaxBidOnItem FROM Item
JOIN Bid ON Bid.RelatedItemID = Item.ItemID
WHERE BidStartDate IS NOT NULL AND SYSDATE() < BidEndDate AND RelatedBuyer = 'TestBuyer1'
GROUP BY Item.ItemID
) AS GetItemsMaxBids;


SELECT IFNULL(SUM(MaxBidOnItem), 0) FROM
(SELECT Item.ItemID, MAX(AmountBid) AS MaxBidOnItem FROM Item
JOIN Bid ON Bid.RelatedItemID = Item.ItemID
WHERE BidStartDate IS NOT NULL AND ActivityStatus = 'Completed' AND isFrozen = False
GROUP BY Item.ItemID
) AS GetItemsMaxBids;


-- Test1: Buyer cannot place a bid below the item's original price.

-- Test2: Buyer cannot place a bid for an item that they cannot afford 
-- due to its price being higher than the amount of funds they have.

-- Test3: Buyer can place a bid for an item that they can afford 
-- as they have not already placed bids anywhere else.

-- Test4: Buyer cannot place a bid for an item that they have the current
-- highest bid for.

INSERT INTO Bid (RelatedItemID, AmountBid, RelatedBuyer, PlacementDate) VALUES (2, 235, 'Buyer1', SYSDATE());

DELETE FROM Seller WHERE Username = 'Admin1';

DELETE FROM Buyer WHERE Username = 'Admin1';

SELECT * FROM Item;

SELECT ItemID, Name, AmountBid, PlacementDate
FROM Item
JOIN Bid ON RelatedItemID = ItemID
WHERE ActivityStatus = 'Active' AND RelatedBuyer = 'Buyer1';