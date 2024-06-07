document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem('token');
    if (token) {
        document.getElementById('loginbotao').style.display = 'none';
        console.log('token checado');
    } else {
        window.location.href = '/Codigo/CodigosFrontEnd/Login/novologin.html';
    }

    // Requisição GET para pegar TODOS os eventos
    fetch('http://localhost:6789/casa/getAll', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (!response.ok) {
            throw new Error('Erro na requisição: ' + response.statusText);
        }
        return response.json();
    }).then(data => {
        console.log('Resposta do servidor:', data);
        const container = document.getElementById('cards-container');
        data.forEach(evento => {
            const card = document.createElement('div');
            card.className = 'card';
            card.dataset.id = evento.id; // Adiciona o ID do evento ao dataset do card

            const horario = evento.horario ? formatHorario(evento.horario) : 'Horário não disponível';

            card.innerHTML = `
                <h2>${evento.nome}</h2>
                <p>${evento.endereco}</p>
                <p>Valor: R$${evento.valor},00</p>
                <p>Horário: ${horario}</p>
                <p>Responsável: ${evento.nomeDono}</p>
                <p>Contato: ${evento.telefonedono}</p>
            `;
            container.appendChild(card);
        });
    }).catch(error => {
        console.error('Erro ao buscar dados dos eventos:', error);
        const errorMsg = document.createElement('p');
        errorMsg.textContent = 'Erro ao carregar os eventos. Tente novamente.';
        document.getElementById('cards-container').appendChild(errorMsg);
    });

    // Adiciona evento de clique nos cards
    document.getElementById('cards-container').addEventListener('click', function(event) {
        const card = event.target.closest('.card');
        if (card) {
            const id = card.dataset.id;
            window.location.href = `/Codigo/CodigosFrontEnd/detalheAnuncio/detalheAnuncio.html?id=${id}`;
        }
    });
});

$(document).ready(function () {
    // Inicialize o botão de colapso de barra lateral
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
    });
});

function formatHorario(horario) {
    console.log('Horário:', horario);
    if (!horario || typeof horario !== 'object' || !('hour' in horario) || !('minute' in horario) || !('second' in horario)) {
        console.error('Horário inválido:', horario);
        return 'Horário não disponível';
    }
    
    const { hour, minute, second } = horario;

    // Cria um objeto Date usando os valores fornecidos
    // Aqui assumimos uma data padrão, pois só precisamos do horário
    const date = new Date();
    date.setHours(hour);
    date.setMinutes(minute);
    date.setSeconds(second);

    // Formata a hora como string
    const options = {
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    };

    // Retorna a hora formatada
    return date.toLocaleString('pt-BR', options);
}
