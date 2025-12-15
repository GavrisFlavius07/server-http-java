const products = [
  {
    productName: "OXFORD SHIRT TOMMY HILFIGER",
    category: "Shirts",
    price: "30$",
    image: "./images/oxford.webp",
    size: ["S", "M", "XXL"],
    sale: true,
  },
  {
    productName: "GANT GINGHAM SHIRT",
    category: "Shirts",
    price: "49$",
    image: "./images/gant.webp",
    size: ["L"],
    sale: false,
  },
  {
    productName: "LUMBERJACK SHIRT",
    category: "Shirts",
    price: "49$",
    image: "./images/lumberjackshirt.webp",
    size: ["S"],
    sale: false,
  },
  {
    productName: "BOSS SLIM FIT JEANS",
    category: "Jeans",
    price: "99$",
    image: "./images/boss.webp",
    size: ["44", "52"],
    sale: false,
  },
  {
    productName: "ARMANI POCKETS PANT",
    category: "Jeans",
    price: "129$",
    image: "./images/armani.webp",
    size: ["48", "50", "52"],
    sale: false,
  },
  {
    productName: "RALPH LAUREN AERA LACE",
    category: "Shoes",
    price: "129$",
    image: "./images/polo.webp",
    size: ["38", "39", "43", "44"],
    sale: true,
  },
  {
    productName: "RALPH LAUREN HERITAGE",
    category: "Shoes",
    price: "110$",
    image: "./images/polo2.webp",
    size: ["44"],
    sale: false,
  },
  {
    productName: "CLOSURE JACKET LONDON",
    category: "Jackets",
    price: "189$",
    image: "./images/london.webp",
    size: ["L", "XL"],
    sale: false,
  },
  {
    productName: "JACK JONES LONG PUFFER",
    category: "Jackets",
    price: "77$",
    image: "./images/jack.webp",
    size: ["S", "M", "L", "XL"],
    sale: false,
  },
  {
    productName: "POLO RALPH LAUREN ROUND",
    category: "Sunglasses",
    price: "111$",
    image: "./images/sunRalph.webp",
    size: [""],
    sale: true,
  },
  {
    productName: "ROLEX DAYTONA",
    category: "Watches",
    price: "20.000$",
    image: "./images/rolex.png",
    size: [""],
    sale: true,
  },
];

let new_div;
let new_img;
let new_h1;
let new_p;
let sale_text;
let new_price;

function creaProdotti(prodotto) {
  new_div = document.createElement("div");
  new_div.classList.add("container","col-md-4", "col-sm-6");

  new_img = document.createElement("img");
  new_img.setAttribute("src", prodotto.image);
  new_img.classList.add("w-100");

  new_h1 = document.createElement("h1");
  new_h1.innerHTML = prodotto.productName;

  new_p = document.createElement("p");
  new_p.innerHTML = prodotto.size;

  new_price = document.createElement("p");
  new_price.innerHTML = prodotto.price;

  sale_text = document.createElement("p");

  if (prodotto.sale) {
    new_div.style.border = "2px solid red";
    sale_text.innerHTML = "In vendita";
    sale_text.style.color = "red";
    new_div.appendChild(sale_text);
  }

  new_div.appendChild(new_img);
  new_div.appendChild(new_h1);
  new_div.appendChild(new_p);
  new_div.appendChild(new_price);
  new_div.appendChild(sale_text);
  document.getElementById("product-content").appendChild(new_div);
}


function eliminaTutto() {
  const productContent = document.getElementById("product-content");
  productContent.innerHTML = "";
}

function stampaProdotti() {
  for (let i = 0; i < products.length; i++) {
    creaProdotti(products[i]);
  }
}

document.addEventListener("DOMContentLoaded", function () {
  stampaProdotti();
});

function carica(cat) {
  eliminaTutto();

  for (let i = 0; i < products.length; i++) {
    if (products[i].category == cat) {
      creaProdotti(products[i]);
    }
  }
}

function cerca() {
  const ricerca = document.getElementById("ricerca").value.toLowerCase();

  eliminaTutto();

  const filteredProducts = products.filter(product => product.productName.toLowerCase().includes(ricerca));

  filteredProducts.forEach((product) => {
    creaProdotti(product);
  });
}

document.getElementById("ricercaForm").addEventListener("submit", function (e) {
  e.preventDefault();
  cerca();
});
