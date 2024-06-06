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
        const anuncioDetails = `
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title text-center">${anuncio.nome}</h5>
                    <p class="card-text">${anuncio.endereco}</p>
                    <p class="card-text">Data: ${formatHorario(anuncio.horario)}</p>
                    <p class="card-text">Local: ${anuncio.endereco}</p>
                </div>
            </div>
        `;
        container.innerHTML += anuncioDetails;
    }
    );
}
).catch(error => {
    console.error('Erro ao buscar anuncios:', error);
    const errorMsg = document.createElement('p');
    errorMsg.textContent = 'Erro ao carregar os anuncios. Tente novamente.';
    document.getElementById('anuncios-container').appendChild(errorMsg);
}
);
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
