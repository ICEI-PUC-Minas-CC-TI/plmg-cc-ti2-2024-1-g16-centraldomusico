const token = localStorage.getItem('token');
let bandasData = [];

document.addEventListener('DOMContentLoaded', function() {
    if (token) {
        document.getElementById('loginbotao').style.display = 'none';
        console.log('token checado');
    } else {
        window.location.href = '/Codigo/CodigosFrontEnd/Login/novologin.html';
    }

    fetch('http://localhost:6789/banda/getAll', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro na requisição: ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        bandasData = data; // Armazena os dados das bandas
        console.log('Resposta do servidor:', data);
        renderBandas(data); // Renderiza as bandas no início
    })
    .catch(error => {
        console.error('Erro ao buscar dados das bandas:', error);
        const errorMsg = document.createElement('p');
        errorMsg.textContent = 'Erro ao carregar as bandas. Tente novamente.';
        document.getElementById('cards-container').appendChild(errorMsg);
    });
});

function renderBandas(bandas) {
    const container = document.getElementById('cards-container');
    container.innerHTML = ''; // Limpa o container antes de renderizar

    bandas.forEach(banda => {
        const card = document.createElement('div');
        card.className = 'card';
        card.innerHTML = `
            <h2>${banda.nomeBanda}</h2>
            <p>${banda.descricao}</p>
            <p>Cache: ${banda.cache}</p>
            <p>Data de Criação: ${banda.dataCriacao}</p>
            <p>Objetivo: ${banda.objetivo}</p>
            <p>Estilo: ${banda.estilo}</p>
        `;
        container.appendChild(card);
    });
}

document.getElementById('cards-container').addEventListener('click', function(event) {
    const card = event.target.closest('.card');
    if (card) {
        const nomeBanda = card.querySelector('h2').textContent;
        window.location.href = `/Codigo/CodigosFrontEnd/encontrebandas/banda.html?nomeBanda=${nomeBanda}`;
    }
});

document.getElementById('search-bar').addEventListener('input', function(event) {
    const searchTerm = event.target.value.toLowerCase();
    const filteredBandas = bandasData.filter(banda => 
        banda.nomeBanda.toLowerCase().includes(searchTerm) ||
        banda.descricao.toLowerCase().includes(searchTerm) ||
        banda.cache.toString().toLowerCase().includes(searchTerm) ||
        banda.dataCriacao.toLowerCase().includes(searchTerm) ||
        banda.objetivo.toLowerCase().includes(searchTerm) ||
        banda.estilo.toLowerCase().includes(searchTerm)
    );
    renderBandas(filteredBandas);
});
