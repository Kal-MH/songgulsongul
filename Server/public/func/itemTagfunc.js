const itemName = document.querySelector(".itemName");
const naverApiBtn = document.querySelector(".sendNaverApiBtn");

const resultDivs = document.querySelectorAll(".resultTable");

function handleNaverApi() {
    const itemNameValue = itemName.value;

    fetch(
        `http://localhost:3000/api/naver/item-tag?item=${itemNameValue}`
    ).then(function (response) {
        return response.json();
    }).then(function (json) {  
        console.log(json)  
        for (var i = 0; i < json.result.length; i++){
            resultDivs[i].children[0].innerText = json.result[i].title;
            resultDivs[i].children[1].innerText = json.result[i].maker;
            resultDivs[i].children[2].innerText = json.result[i].mallName;
            resultDivs[i].children[3].innerText = json.result[i].lprice;
            resultDivs[i].children[4].src = json.result[i].image;
            resultDivs[i].children[5].href = json.result[i].link;
        }
    })
    // fetch(
    //     `https://openapi.naver.com/v1/search/shop.json?query=${itemNameValue}&display=10&start=1&sort=sim`,
    //     {
    //         mode: 'no-cors',
    //         headers: {
    //             "Access-Control-Allow-Origin" : "http://localhost",
    //             "X-Naver-Client-Id" : CLIENTID,
    //             "X-Naver-Client-Secret" : cLIENTPASSWORD

    //         }
    //     }
    // ).then(function (response) {
    //     console.log(response);
    //     return response.json();
    // }).then(function (json) {
    //     console.log(json);
    // })
}

function init() {
    //console.dir(resultDiv)
    naverApiBtn.addEventListener("click", handleNaverApi);
}

init();