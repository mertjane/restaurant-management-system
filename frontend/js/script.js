const title = "23x48x10-290x290x10";
const mosaicWidth = parseInt(title.split("-")[1].split("x")[0]);
const mosaicHeight = parseInt(title.split("-")[1].split("x")[1]);
console.log("width:", mosaicWidth, "\nheight:", mosaicHeight);

const quantity = 3;

const mosaicCurrentArea = (mosaicWidth * mosaicHeight) / 1_000_000;
const totalMosaicArea = mosaicCurrentArea * quantity;

const mosaicSqmValue = parseFloat(totalMosaicArea.toFixed(3));

console.log(mosaicSqmValue);
