import React, { useState, useEffect } from "react";
import "./header.css";
import "./spoonMenu.css";

/* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% */

/* %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% */
function Header(props) {
  const [spoonsMenu, setSpoonsMenu] = useState(false);

  const openSpoonsMenu = () => {
    document.querySelector(".spoon_menu").classList.toggle("open");
    document.querySelector("#invoice-mobile-background").classList.toggle("show");

    if (spoonsMenu) setSpoonsMenu(false);

    setSpoonsMenu(true);
  };

  useEffect(() => {
    if (props.gameStarted) {
      document.querySelector("#invoice-mobile-background").classList.remove("show");
      document.querySelector(".spoon_menu").classList.remove("open");
    }
  }, [props.gameStarted]);

  return (
    <header>
      {/* <h2>Sopa de Letras</h2> */}
      <div className="spoon_menu" onClick={openSpoonsMenu}>
        <div></div>
        <div></div>
      </div>
    </header>
  );
}

export default Header;
