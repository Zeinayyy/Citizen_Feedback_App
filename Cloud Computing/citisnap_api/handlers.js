const indexHandler = (request,h)=>{
    const response = h.response({
        "status": "success",
        "message": "Api Berhasil Dipanggil",
        "data": {}
    })
    response.code(201)
    return response
}
module.exports = {indexHandler}