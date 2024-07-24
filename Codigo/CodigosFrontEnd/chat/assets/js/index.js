const msRest = require('@azure/ms-rest-js');
const PredictionApi = require('@azure/cognitiveservices-customvision-prediction');

const predictionKey = '7a74e928ed2a4dcb87a8676ca0f2a0db';
const predictionEndpoint = 'https://ti2reconhecimentofacial.cognitiveservices.azure.com/customvision/v3.0/Prediction/7faff063-f449-4349-8ffc-f99442a067cb/detect/iterations/Iteration6/image';
const projectId = '7faff063-f449-4349-8ffc-f99442a067cb';
const publishIterationName = "Iteration6";

const credentials = new msRest.ApiKeyCredentials({ inHeader: { 'Prediction-key': predictionKey } });
const predictor = new PredictionApi.PredictionAPIClient(credentials, predictionEndpoint);

async function classifyImage(imageBuffer) {
    try {
        const results = await predictor.classifyImage(projectId, publishIterationName, imageBuffer);
        console.log("Results:");
        results.predictions.forEach(predictedResult => {
            console.log(`\t${predictedResult.tagName}: ${(predictedResult.probability * 100.0).toFixed(2)}%`);
        });
    } catch (error) {
        console.error("Error classifying image:", error);
    }
}

async function enviarfoto(event) {
    event.preventDefault();
    const input = document.getElementById('fotoPerfil');
    const file = input.files[0];
    if (!file) {
        alert("Por favor, selecione uma imagem para enviar.");
        return;
    }
    const reader = new FileReader();
    reader.onload = async function() {
        const imageBuffer = Buffer.from(reader.result);
        await classifyImage(imageBuffer);
    };
    reader.readAsArrayBuffer(file);
}

function previewImage(event) {
    const preview = document.getElementById('preview');
    const reader = new FileReader();
    reader.onload = function() {
        if (reader.readyState == 2) {
            preview.src = reader.result;
            preview.style.display = 'block';
        }
    }
    reader.readAsDataURL(event.target.files[0]);
}