//Definir frases aleatorias

const Frases = [
    "Olá como vai?",
    "Vamos marcar algo",
    "O que você toca?",
    "lasldasldsl"

]

//Fim definir

function gerarFrase() {
    const indice = Math.floor(Math.random() * Frases.length);
    return Frases[indice];
}



//fim

function sendMessage() {
    var message = document.getElementById('message-input');
    if (!message.value) {
        message.style.border = '1px solid red'
        return
    }
    message.style.border = "none"

    //requisição da mensagem digitada pelo usuário

    var userMessage = message.value;
    showHistoric(userMessage)
    message.value = ' ';

    //fim

    var status = document.getElementById('status');
    var button = document.getElementById('button');
    button.disabled = true;
    button.style.cursor = 'not-allowed'

    setTimeout(function () {
        button.disabled = false;
        button.style.cursor = 'pointer';
    }, 2000);
}

function showHistoric(message) {
    var historic = document.getElementById('historic')

    //minhas mensagens

    var boxMyMessage = document.createElement('div')
    boxMyMessage.className = 'box-my-message'

    var MyMessage = document.createElement('p')
    MyMessage.className = 'my-message'
    MyMessage.innerHTML = message

    boxMyMessage.appendChild(MyMessage)
    historic.appendChild(boxMyMessage)

    //r espostas 

    var RespostaMessage = document.createElement('div')
    RespostaMessage.className = 'box-response-message'

    var chatResponse = document.createElement('p')
    chatResponse.className = 'chat-response'


    chatResponse.innerHTML = 'Digitando...';


    setTimeout(function () {
        historic.removeChild(AnimationMessage)
        chatResponse.innerHTML = gerarFrase();
        chatResponse.style.marginLeft = '-5%'
    }, 3000);

    RespostaMessage.appendChild(chatResponse)
    historic.appendChild(RespostaMessage)


    //animação

    var AnimationMessage = document.createElement('div')
    AnimationMessage.className = 'box-animation-message'

    var animation = document.createElement('p')
    animation.className = 'animação';


    AnimationMessage.appendChild(animation)
    historic.appendChild(AnimationMessage)


    //fim animação


    //Caixa de escolha



}

function selecionar() {
    let pessoas = document.getElementById("pessoas");
    let bandas = document.getElementById("bandas");
    var friends = document.getElementsByClassName('pessoa1');
    var header = document.getElementById('header1'); // Seleciona o elemento header existente
    var name_friend = document.createElement('p');
    name_friend.className = 'name_friend1';

    name_friend.innerHTML = '';

    for (var i = 0; i < friends.length; i++) {
        friends[i].style.display = 'block';

        friends[i].addEventListener('click', function (event) {
            Nome(event.target.innerText);
        });
    }


    pessoas.addEventListener('click', function () {

        if (pessoas.checked === true) {
            bandas.checked = false;
            pessoas.checked = true;

        } else if (bandas.checked === true) {
            pessoas.checked = false;
            bandas.checked = true;

        }


    })

    bandas.addEventListener('click', function () {
        if (bandas.checked === true) {
            pessoas.checked = false;
            bandas.checked = true;

            for (var i = 0; i < friends.length; i++) {
                friends[i].style.display = 'none';
            }

        } else if (pessoa.checked === true) {
            pessoas.checked = false;
            bandas.checked = true;
        }


    })

    for (var i = 0; i < friends.length; i++) {
        Limpar();
    }

}

function Nome(amigo) {
    var name_friend = document.createElement('p');
    name_friend.className = 'name_friend1';
    name_friend.innerHTML = amigo;

    // Remove o conteúdo anterior do header
    var header = document.getElementById('header1');
    header.innerHTML = '';

    // Adiciona o novo nome ao header
    header.appendChild(name_friend);

}


function Limpar(clique) {
    var historic = document.getElementById('historic');
    historic.innerHTML = '';
}



function goBack() {
    window.history.back();
}



