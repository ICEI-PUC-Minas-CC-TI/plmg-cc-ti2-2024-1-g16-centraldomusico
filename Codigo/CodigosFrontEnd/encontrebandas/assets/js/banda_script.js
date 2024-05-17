$(document).ready(function () {
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
    });
});

function alerta () {
    alert("Pedido enviado!!!");
}

document.addEventListener('DOMContentLoaded', function () {
const resultadobanda = document.getElementById('resultadobanda');

fetch('https://jsonservercentraldomusico.arthurcarvalh19.repl.co/banda')
        .then(response => {
            if (!response.ok) {
                throw new Error(`Erro ao carregar o arquivo JSON: ${response.status}`);
            }
            return response.json();
        })
        .then(banda => {
            banda.forEach(uni => {
                const bandaHTML = `<div class="card" style="width: 22rem;">                 
                <div class="card-body">
                    <h5 class="card-title">${uni.nomebanda}</h5>
                    <h5 class="card-title">${uni.genero}</h5>
                    <p class="card-text">${uni.descricao}</p>
                    <button onclick="alerta()" class="btn btn-dark expandir-btn">Inscrever-se</button>
                </div>
            </div>`;
            resultadobanda.innerHTML += bandaHTML;

            });

 })
    
});