$(document).ready(function () {
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
    });
});


function expandir(id) {
    const popupWrapper = document.getElementById('popup-wrapper');

    popupWrapper.style.display = 'block';

    // Aqui você pode adicionar a lógica de fetch para obter dados específicos com base no ID
    fetch(`https://jsonservercentraldomusico.arthurcarvalh19.repl.co/perfil/${id}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Erro ao carregar o arquivo JSON: ${response.status}`);
            }
            return response.json();
        })
        .then(perfil => {
            const popupHTML = `
                <div class="popup">
                    <div class="popup-content">
                    
                        <div class="popup-close" onclick="fecharPopup()">x</div>
                            
                        
                                <h3>Perfil Semi-Expandido</h3>
                                <img src="https://thispersondoesnotexist.com">
                                <h5>${perfil.nome}</h5>
                                <div class="minidescricao">
                                    <h6>Descrição:</h6>
                                    <p>${perfil.descricao}</p>
                                </div>
                                <div class="botao-perfil">
                        
                        <a href="/perfil/perfil.html?id=${perfil.id}">Ir para o perfil</a>
                        </div>       

                    </div>
                </div>`;

            // Adicione o HTML do popup à div de contêiner
            popupWrapper.innerHTML = popupHTML;
        })
        .catch(error => {
            console.error(error);
        });
}

function fecharPopup() {
    const popupWrapper = document.getElementById('popup-wrapper');
    popupWrapper.style.display = 'none';
}

document.addEventListener('DOMContentLoaded', function () {
    const searchInput = document.getElementById('txtBusca');
    const filteredCardsContainer = document.getElementById('resultadoPesquisa');
    const allCardsContainer = document.getElementById('resultadoPesquisa');

    function renderCards(cards, container) {
        container.innerHTML = ``;
        cards.forEach(card => {
            const cardHTML = `<div class="card" style="width: 22rem;">
                <img src="https://thispersondoesnotexist.com" class="card-img-top" alt="...">
                <div class="card-body">
                    <h5 class="card-title">${card.nome}</h5>
                    <h5 class="card-title">${card.instrumento1}</h5>
                    <p class="card-text">${card.descricao}</p>
                    <button data-card-id="${card.id}" class="btn btn-dark expandir-btn" style="width: 100%;">Expandir</button>
                </div>
            </div>`;
            container.innerHTML += cardHTML;
        });
    }
    

    function filterCards(searchTerm) {
        const filteredCards = jsonData.filter(card =>
            card.nome.toLowerCase().includes(searchTerm.toLowerCase())
        );
        renderCards(filteredCards, filteredCardsContainer);
    }

    searchInput.addEventListener('input', function () {
        const searchTerm = this.value.trim();
        filterCards(searchTerm);
    });

    document.addEventListener('click', function (event) {
        if (event.target.classList.contains('expandir-btn')) {
            const cardId = event.target.dataset.cardId;
            expandir(cardId);
        }
    });

    fetch('https://jsonservercentraldomusico.arthurcarvalh19.repl.co/perfil')
        .then(response => {
            if (!response.ok) {
                throw new Error(`Erro ao carregar o arquivo JSON: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            jsonData = data;
            renderCards(data, allCardsContainer);
        })
        .catch(error => {
            console.error('Erro ao obter os dados JSON:', error);
        });
});



function filterBlocs() { 
    var selectElement = document.getElementById('filtroEstilo');
    var valorSelecionado = selectElement.value;
    var textoSelecionado = selectElement.options[selectElement.selectedIndex].text;
    console.log("Texto selecionado: " + textoSelecionado);
    var selectElement2 = document.getElementById('filtroInstrumento');
    var valorSelecionado2 = selectElement2.value;
    var textoSelecionado2 = selectElement2.options[selectElement2.selectedIndex].text;
    console.log("Texto selecionado: " + textoSelecionado2)
    var selectElement3 = document.getElementById('filtroEstado');
    var valorSelecionado3 = selectElement3.value;
    var textoSelecionado3 = selectElement3.options[selectElement3.selectedIndex].text;
    console.log("Texto selecionado: " + textoSelecionado3)
    
    filterCardss(textoSelecionado2);
    

    

    
}
function filterCardss(textoSelecionado2) {
    var filteredCards = jsonData.filter(card =>
        card.instrumento.toLowerCase().includes(textoSelecionado2.toLowerCase())
    );
    renderCards(filteredCards, filteredCardsContainer);
    console.log(filteredCards);
} // Aqui você pode chamar a lógica de filtragem ou fazer o que for necessário com os valores selecionados