import React from "react";

export default function ItemFilter({chosenFilter, setChosenFilter}: {chosenFilter: string, setChosenFilter: React.Dispatch<React.SetStateAction<string>>}) { 

    return (
        <div>
            <label htmlFor="filterOptions" className="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Select a Filter Option from the Dropdown below:</label>
            <select id="filterOptions" value={chosenFilter} onChange={(event) => setChosenFilter(event.target.value)} className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500 mb-6">
                <option value="">Choose a Filter</option>
                <option value="Inactive">Inactive</option>
                <option value="Active">Active</option>
                <option value="Failed">Failed</option>
                <option value="Completed">Completed</option>
                <option value="Archived">Archived</option>
                <option value="Frozen">Frozen</option>
            </select>
        </div >
    );
}

