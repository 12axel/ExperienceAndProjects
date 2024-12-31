'use client'
import Navbar from "./components/Navbar";
import React from "react";

export default function Home() {
  const [userType, setUserType] = React.useState<string | null>(null);
  const [userName, setUserName] = React.useState<string | null>(null);

  React.useEffect(() => {
      setUserType(sessionStorage.getItem('userType'));
      setUserName(sessionStorage.getItem('userName'));
  }, []);
  return (
    <main>
      <div className="container mx-auto mt-5">
        <h1 className="text-4xl">welcome</h1>
      </div>
    </main>
  );
}
