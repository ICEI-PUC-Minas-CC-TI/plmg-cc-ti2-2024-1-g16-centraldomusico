document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem('token');
    if (token) {
        document.getElementById('loginbotao').style.display = 'none';
        console.log('token checado');
    } else {
        window.location.href = '/Codigo/CodigosFrontEnd/Login/novologin.html';
    }
        //Requisição GET para pegar TODOS os eventos
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
        }
        ).then(data => {
            console.log('Resposta do servidor:', data);
            const container = document.getElementById('cards-container');
            data.forEach(evento => {
                const card = document.createElement('div');
                card.className = 'card';

                card.innerHTML = `
                        <h2>${evento.nome}</h2>
                        <p>${evento.endereco}</p>
                        <p>Valor: R$${evento.valor},00</p>
                        <p>Dia e Horário: ${formatHorario(evento.horario)}</p>
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
});
$(document).ready(function () {
    // Inicialize o botão de colapso de barra lateral
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
    });

});



function formatHorario(horario) {
    const { year, month, day } = horario.date;
    const { hour, minute, second } = horario.time;

    // Cria um objeto Date usando os valores fornecidos
    const date = new Date(year, month - 1, day, hour, minute, second);

    // Formata a data e a hora como string
    const options = {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    };

    return date.toLocaleString('pt-BR', options);
}