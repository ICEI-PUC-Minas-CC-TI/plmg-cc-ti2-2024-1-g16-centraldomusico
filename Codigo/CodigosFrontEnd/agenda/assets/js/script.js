document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem('token');
    if (token) {
        document.getElementById('loginbotao').style.display = 'none';
        console.log('token checado');
    } else {
        window.location.href = '/Codigo/CodigosFrontEnd/Login/novologin.html';
    }
});
const idBanda = localStorage.getItem('bandaId');

$(document).ready(function () {
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
    });
});

document.addEventListener('DOMContentLoaded', function () {
    //mostrar anuncios dos quais a banda do usuario esta inscrita,
    fetch(`http://localhost:6789/casa/getAnunciosBanda?id=${idBanda}`, {
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
        const container = document.getElementById('anuncios-container');
        console.log('Resposta do servidor:', data);
        data.forEach(anuncio => {
            console.log('Anuncio:', anuncio);

            // Ensure horario is defined and structured correctly
            const horario = anuncio.horario ? formatHorario(anuncio.horario) : 'Horário não disponível';

            const anuncioDetails = `
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title text-center">${anuncio.nome}</h5>
                        <p class="card-text">${anuncio.endereco}</p>
                        <p class="card-text">Horário: ${horario}</p>
                        <p class="card-text">Local: ${anuncio.endereco}</p>
                    </div>
                </div>
            `;
            container.innerHTML += anuncioDetails;
        });
    }).catch(error => {
        console.error('Erro ao buscar anuncios:', error);
        const errorMsg = document.createElement('p');
        errorMsg.textContent = 'Erro ao carregar os anuncios. Tente novamente.';
        document.getElementById('anuncios-container').appendChild(errorMsg);
    });
});

function formatHorario(horario) {
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
