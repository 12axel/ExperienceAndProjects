'use client'
import React, { useState, useEffect } from "react";
import axios from "axios";
import { useRouter } from 'next/navigation';
import LoadingSpinner from "../components/LoadingSpinner";
const baseURL = "https://ziek69aur9.execute-api.us-east-2.amazonaws.com/initial";

export default function AddItem() {
  const [itemName, setItemName] = React.useState<string>('');
  const [initialPrice, setInitialPrice] = React.useState<string>('');
  const [itemDescription, setItemDescription] = React.useState<string>('');
  const [bidEndDate, setBidEndDate] = React.useState<string>('');
  const [isABuyNow, setIsABuyNow] = React.useState<number>(0);
  const [imageFiles, setImageFiles] = React.useState<File[]>([]);
  const [addItemButtonDisabled, setAddItemButtonDisabled] = React.useState<boolean>(true);
  const imageUploadRef = React.useRef<HTMLInputElement | null>(null);
  const [submissionLoading, setSubmissionLoading] = React.useState<boolean>(false);
  const [currentUser, setCurrentUser] = useState<string>('');
  const [currentPassword, setCurrentPassword] = useState<string>('');
  const router = useRouter();


  useEffect(() => {
    const currentUsername = sessionStorage.getItem('userName');
    const currentPassword = sessionStorage.getItem('password');
    if (currentUsername && currentPassword) {
      setCurrentUser(currentUsername);
      setCurrentPassword(currentPassword);
    }

    if (itemName !== '' && initialPrice !== '' && itemDescription !== '' && bidEndDate !== '' && imageFiles.length > 0) {
      setAddItemButtonDisabled(false);
    } else {
      setAddItemButtonDisabled(true);
    }
  }, [itemName, initialPrice, itemDescription, bidEndDate, imageFiles]);

  const getPresignedURL = async (file: File) => {
    try {
      const response = await axios.post(`${baseURL}/upload-url`, {
        body:
        {
          fileName: file.name,
          fileType: file.type,
        }

      });
      console.log(JSON.parse(response.data.body));
      const uploadURL = JSON.parse(response.data.body);

      console.log("Received presigned URL:", uploadURL);
      return uploadURL;
    } catch (error) {
      console.error("Error getting presigned URL:", error);
      return null;
    }
  };


  const uploadImageToS3 = async (file: File, uploadURL: string) => {
    await axios.put(uploadURL, file, {
      headers: {
        'Content-Type': file.type,
      },
    });
    return uploadURL.split('?')[0];
  };

  const addItem = async (event: React.FormEvent<HTMLFormElement>) => {
    const bidEndDateUTC = new Date(bidEndDate).toISOString();
    event.preventDefault();

    setSubmissionLoading(true);

    const addBasicItemInfo = async () => {
      const payload = {
        body: {
          username: currentUser,
          password: currentPassword,
          itemName: itemName,
          initialPrice: initialPrice,
          isBuyNow: isABuyNow,
          itemDescription: itemDescription,
          bidEndDate: bidEndDateUTC,
        },
      };

      try {
        const response = await axios.post(`${baseURL}/add-item`, payload, {
          headers: {
            'Content-Type': 'application/json',
          },
        });
        return JSON.parse(response.data.body);
      } catch (error) {
        console.error("Error adding item:", error);
      }
    };

    const basicItemInfo = await addBasicItemInfo();
    if (!basicItemInfo) {
      console.error("Failed to add item.");
      return;
    }

    sessionStorage.setItem('basicItemInfo', JSON.stringify(basicItemInfo));
    console.log("sessionStoragePayload: ", sessionStorage.getItem('basicItemInfo'));

    const itemId = basicItemInfo.ItemID;
    console.log("Item created with ID:", itemId);

    const imageURLs = await Promise.all(
      imageFiles.map(async (file) => {
        const uploadURL = await getPresignedURL(file);
        return await uploadImageToS3(file, uploadURL);
      })
    );

    console.log("Uploaded image URLs:", imageURLs);

    const addImages = async (itemId: number, imageURL: string) => {
      const payload = {
        body: {
          username: currentUser,
          password: currentPassword,
          relatedItem: itemId,
          URL: imageURL,
        },
      };

      try {
        const response = await axios.post(`${baseURL}/add-item-picture`, payload, {
          headers: {
            'Content-Type': 'application/json',
          },
        });
        console.log("Image added to Picture table:", response.data);
      } catch (error) {
        console.error("Error adding image:", error);
      }
    };

    for (const imageURL of imageURLs) {
      await addImages(itemId, imageURL);
    }

    console.log("All images associated with ItemID:", itemId);

    // redirect to review-items page upon successfully adding an item
    setSubmissionLoading(false);
    router.push('/review-items');
  };

  return (
    <main>
      <div className="container mx-auto mt-5">
        <h1 className="text-4xl">Add A New Item Here</h1>
        <h2 className="text-3xl text-gray-900 dark:text-white">Please fill out all of the fields in order to add this item</h2>
        {/* Inspiration for styling form fields was taken from the following website: https://flowbite.com/docs/components/forms/ */}
        <form onSubmit={addItem}>
          <div className="grid gap-6 mb-6 md:grid-cols-1">
            <div>
              <label htmlFor="is_buy_now" className="inline-flex items-center cursor-pointer">
                <span className="ms-3 text-sm font-medium text-gray-900 dark:text-gray-300 mr-4">Is it a Buy-Now item? Toggled on = Yes. Toggled off = No.</span>
                <input type="checkbox" id="is_buy_now" checked={isABuyNow === 1} onChange={() => {
                  isABuyNow == 1 ? setIsABuyNow(0) : setIsABuyNow(1);
                }} className="sr-only peer"></input>

                <div className="relative w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 dark:peer-focus:ring-blue-800 rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full rtl:peer-checked:after:-translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:start-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all dark:border-gray-600 peer-checked:bg-blue-600" />
              </label>
            </div>
            <div>
              <label htmlFor="item_name" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Item Name</label>
              <input type="text" id="item_name" value={itemName} onChange={(event) => {
                setItemName(event.target.value);
              }} className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" placeholder="Sample Item Name" required />
            </div>
            <div>
              <label htmlFor="initial_price" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Initial Price</label>
              <input type="text" id="initial_price" value={initialPrice} onChange={(event) => {
                const value = event.target.value.replace(/^0+|[^0-9]/g, "");
                setInitialPrice(value);
              }} className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" placeholder="$0.00" required />
            </div>
            <div>
              <label htmlFor="item_description" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Item Description</label>
              <input type="text" id="item_description" value={itemDescription} onChange={(event) => {
                setItemDescription(event.target.value);
              }} style={{ resize: 'both' }} className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" placeholder="Sample Item Description" required />
            </div>
            <div>
              <label htmlFor="bid_end_date" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">{isABuyNow ? "Purchase End Date" : "Bid End Date"}</label>
              <input type="datetime-local" id="bid_end_date" value={bidEndDate} onChange={(event) => {
                setBidEndDate(event.target.value);
              }} className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" required />
            </div>
            <div>
              <label htmlFor="images" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Item Image(s)</label>
              <input type="file" id="images" ref={imageUploadRef} multiple onChange={(event) => {
                if (event.target.files) {
                  setImageFiles(imageFiles.concat(Array.from(event.target.files)));
                }
              }} className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" required />
            </div>
            {imageFiles.map((imageFile, index) => (
              <div key={index}>
                <img src={URL.createObjectURL(imageFile)} alt={imageFile.name} className="h-auto max-w-full rounded-lg mb-1" />
                <button type="button" className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded" onClick={() => {
                  setImageFiles(imageFiles.filter((file, i) => i !== index));
                }}>Remove</button>
              </div>
            ))}
          </div>
          <div className="flex items-center space-x-4">
            <button type="submit" disabled={addItemButtonDisabled} className={!addItemButtonDisabled ? "text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800 mb-6" : "bg-blue-500 text-white font-bold py-2 px-4 rounded opacity-50 cursor-not-allowed mb-6"}>{submissionLoading ? <LoadingSpinner /> : "Add Item"}</button>
            <div>
              {submissionLoading && <p className="text-blue-700">Currently adding item...</p>}
            </div>
          </div>
        </form>
      </div>
    </main>
  );
}

