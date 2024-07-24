document.addEventListener('DOMContentLoaded', function() {
    const params = new URLSearchParams(window.location.search);
    const id = params.get('id'); // ID da casa de shows
    const token = localStorage.getItem('token');
    const inscreverButton = document.getElementById('inscrever-button');
    const bandaId = localStorage.getItem('bandaId'); // Certifique-se de que o ID da banda está armazenado no localStorage
    if (!token) {
        window.location.href = '/Codigo/CodigosFrontEnd/Login/novologin.html';
        return;
    }else{
        document.getElementById('loginbotao').style.display = 'none';
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
            <p style="color:white">Horário: ${formatHorario(data.horario)}</p>
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

    inscreverButton.addEventListener('click', function() {
        fetch('http://localhost:6789/casa/inscreverBanda', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ casaId: id, bandaId: bandaId })
        }).then(response => {
            if (!response.ok) {
                throw new Error('Erro na requisição: ' + response.statusText);
            }
            return response.json();
        }).then(data => {
            alert('Banda inscrita com sucesso!');
            //adicionar o id da casa no localstorage
            localStorage.setItem('casaId', id);
            //f5
            window.location.reload();
        }).catch(error => {
            console.error('Erro ao inscrever a banda:', error);
            alert('Erro ao inscrever a banda. Tente novamente.');
        });
    });
    
    fetch(`http://localhost:6789/casa/getInscritos?id=${id}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
    }).then(response => {
        if (!response.ok) {
            throw new Error('Erro na requisição: ' + response.statusText);
        }
        return response.json();
    }).then(data => {
        //armazenar todos os inscritos no local storage
        localStorage.setItem('inscritos', JSON.stringify(data));
        const inscritosContainer = document.getElementById('inscritos-container');
        inscritosContainer.innerHTML = '';
        data.forEach(inscrito => {
            const inscritoElement = document.createElement('div');
            //adicionar todas as bandas inscritas no local storage
            inscritoElement.className = 'inscrito';
            console.log('Inscrito:', inscrito);
            inscritoElement.innerHTML = `
                Nome: ${inscrito.nomeBanda}<br>
                Estilo: ${inscrito.estilo}
            `;
            inscritosContainer.appendChild(inscritoElement);
        });
    }).catch(error => {
        console.error('Erro ao buscar inscritos:', error);
        const errorMsg = document.createElement('p');
        errorMsg.textContent = 'Erro ao carregar os inscritos. Tente novamente.';
        document.getElementById('inscritos-container').appendChild(errorMsg);
    });
    //se a banda ja estiver inscrita, mostra o botao de desinscrever
    console.log('id:', id);
    console.log('bandaId:', bandaId);
    desinscreverButton = document.getElementById('desinscrever-button');
    desinscreverButton.addEventListener('click', function() {
        //checar se a banda está inscrita
        //mensagem de confirmação

        var bandaNome = localStorage.getItem('bandaNomeUser');
        var bandasInscritas = JSON.parse(localStorage.getItem('inscritos'));
        var bandaInscrita = bandasInscritas.find(banda => banda.nomeBanda === bandaNome);
        console.log('bandaNome:', bandaNome);
        console.log('bandaInscrita:', bandaInscrita);
        console.log('bandasInscritas:', bandasInscritas);
        if (!bandaInscrita) {
            alert('Banda não inscrita.');
            return;
        }
        if (!confirm('Deseja realmente desinscrever a banda?')) {
            return;
        }
        
        fetch('http://localhost:6789/casa/desinscreverBanda', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ casaId: id, bandaId: bandaId })
        }).then(response => {
            if (!response.ok) {
                throw new Error('Erro na requisição: ' + response.statusText);
            }
            return response.json();
        }).then(data => {
            alert('Banda desinscrita com sucesso!');
            //remover o id da casa do localstorage
            localStorage.removeItem('casaId');
            //f5
            window.location.reload();
        }).catch(error => {
            console.error('Erro ao inscrever a banda:', error);
            alert('Erro ao inscrever a banda. Tente novamente.');
        });
    });
        
});
function inscrever(){

}
function voltar() {
    window.history.back();
}

function formatHorario(horario) {
    console.log(horario);
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
    console.log('HORA', date.toLocaleString('pt-BR', options));
    return date.toLocaleString('pt-BR', options);
}
//ao clicar na banda, redirecionar para a pagina de detalhes da banda
function detalhesBanda(id) {
    window.location.href = `/Codigo/CodigosFrontEnd/encontrebandas/banda.html?id=${id}`;
}
