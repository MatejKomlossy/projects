// Vytvorenie Express aplikacie
const express = require("express");
const app = express();
app.use(express.json()) // req.body

// db
const DB = require("./db")
const db = DB.getDbServiceInstance();

// jednoducha api
app.get("/api", (req,res) => {
    res.json({"skuska":"ano fungujem"})
} )

app.get("/apidb", async (req,res) => {
    try {
        const resoult = await db.get_test();
        console.log(resoult)
        res.json(resoult);
    } catch (err) {
        console.log(err.massage);
    }
} )

app.post("/apidb2", async (req,res) => {
    try {
        const {maximum} = req.body;
        const resoult = await db.get_test2(maximum);
        console.log(resoult)
        res.json(resoult);
    } catch (err) {
        console.log(err.massage);
    }
} )
const PORT = 5000;
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));