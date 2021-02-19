const id = document.querySelector(".id");
const checkDupIdBtn = document.querySelector(".checkDupIdBtn");
const dupIdSpan = document.querySelector(".dupIdSpan");

const email = document.querySelector(".email");
const sendAuthEmailBtn = document.querySelector(".sendAuthEmailBtn");

const emailAuthNumber = document.querySelector(".emailAuthNumber");
const emailAuthNumberBtn = document.querySelector(".emailAuthBtn");

const password = document.querySelector(".password");

const joinBtn = document.querySelector(".joinBtn");

// form method로 대체됨
function handleJoinBtnClick() {
    const LoginidValue = id.value;
    const passwordValue = password.value;
    const emailValue = email.value;

    fetch(
        `http://localhost:3000/join`,
        {
            method: 'POST',
            mode : 'cors',
            headers: {
                'Content-Type' : 'application/json',
            },
            body: JSON.stringify({
                loginId : LoginidValue,
                password : passwordValue,
                email : emailValue
            })
        }
        ).then(function (response) {
            console.log(response);
            return response.json();
        }).then(function (json) {
            console.log(json);
        })
}

function handleDupIdBtnClick() {
    const idValue = id.value;

    fetch(
        `http://localhost:3000/api/dup-idcheck`,
        {
            method: 'POST',
            mode : 'cors',
            headers: {
                'Content-Type' : 'application/json',
            },
            body: JSON.stringify({
                id : idValue
            })
        }
    ).then(function (response) {
        return response.json();
    }).then(function (json) {
        console.log(json)
        if (json.code == 200){
            dupIdSpan.innerText = "사용가능함";
        } else {
            dupIdSpan.innerText = "중복아이디";
        }
    })

}

function handleSendAuthEmailBtnClick() {
    const emailValue = email.value;

    fetch(
        `http://localhost:3000/api/email-auth`,
        {
            method: 'POST',
            mode : 'cors',
            headers: {
                'Content-Type' : 'application/json',
            },
            body: JSON.stringify({
                email : emailValue
            })
        }
        ).then(function (response) {
            console.log(response);
            return response.json();
        }).then(function (json) {
            console.log(json);
        })
}

function handleEmailAuthNumberBtnClick() {
    const authNumberValue = emailAuthNumber.value;

    fetch(
        `http://localhost:3000/api/email-auth-number`,
        {
            method: 'POST',
            mode : 'cors',
            headers: {
                'Content-Type' : 'application/json',
            },
            body: JSON.stringify({
                authNumber : authNumberValue
            })
        }
        ).then(function (response) {
            console.log(response);
            return response.json();
        }).then(function (json) {
            console.log(json);
        })
}

function init() {
    checkDupIdBtn.addEventListener("click", handleDupIdBtnClick);
    sendAuthEmailBtn.addEventListener("click", handleSendAuthEmailBtnClick)
    emailAuthNumberBtn.addEventListener("click", handleEmailAuthNumberBtnClick)
    //joinBtn.addEventListener("click", handleJoinBtnClick);
}

init();