document.addEventListener('DOMContentLoaded', function() {
    const params = new URLSearchParams(window.location.search);
    const id = params.get('id');
    const token = localStorage.getItem('token');

    if (!token) {
        window.location.href = '/Codigo/CodigosFrontEnd/Login/novologin.html';
        return;
    }

    fetch(`http://localhost:6789/casa/getCasa?id=${id}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    }).then(response => {
        if (!response.ok) {
            throw new Error('Erro na requisição: ' + response.statusText);
        }
        return response.json();
    }).then(data => {
        const container = document.getElementById('anuncio-container');
        console.log('Resposta do servidor:', data);
        const anuncioDetails = `
    
            <h1 style="color:white">${data.nome}</h1>
            <p style="color:white">Endereço: ${data.endereco}</p>
            <p style="color:white">Valor: R$${data.valor.toFixed(2).replace('.', ',')}</p>
            <p style="color:white">Dia e Horário: ${formatHorario(data.horario)}</p>
            <p style="color:white">Responsável: ${data.nomeDono}</p>
            <p style="color:white">Contato: ${data.telefonedono}</p>
        `;
        container.innerHTML = anuncioDetails;
    }).catch(error => {
        console.error('Erro ao buscar detalhes do anúncio:', error);
        const errorMsg = document.createElement('p');
        errorMsg.textContent = 'Erro ao carregar os detalhes do anúncio. Tente novamente.';
        document.getElementById('anuncio-container').appendChild(errorMsg);
    });

});

function voltar() {
    window.history.back();
}

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
