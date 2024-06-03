

const token = localStorage.getItem('token');
document.addEventListener('DOMContentLoaded', function() {
    if(token){
        document.getElementById('loginbotao').style.display = 'none';
        console.log('token checado');
    }else{
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
            console.log('Resposta do servidor:', data);
            const container = document.getElementById('cards-container');
            data.forEach(banda => {
                const card = document.createElement('div');
                card.className = 'card';
                card.innerHTML = `
                    <h2 style>${banda.nomeBanda}</h2>
                    <p>${banda.descricao}</p>
                    <p>Cache: ${banda.cache}</p>
                    <p>Data de Criação: ${banda.dataCriacao}</p>
                    <p>Objetivo: ${banda.objetivo}</p>
                    <p>Estilo: ${banda.estilo}</p>
                `;
                container.appendChild(card);
            });
        })
        .catch(error => {
            console.error('Erro ao buscar dados das bandas:', error);
            const errorMsg = document.createElement('p');
            errorMsg.textContent = 'Erro ao carregar as bandas. Tente novamente.';
            document.getElementById('cards-container').appendChild(errorMsg);
        });
});

//ao clicar em um card, redireciona para a página da banda
document.getElementById('cards-container').addEventListener('click', function(event) {
    const card = event.target.closest('.card');
    if (card) {
        const nomeBanda = card.querySelector('h2').textContent;
        window.location.href = `/Codigo/CodigosFrontEnd/encontrebandas/banda.html?nomeBanda=${nomeBanda}`;
    }
});
