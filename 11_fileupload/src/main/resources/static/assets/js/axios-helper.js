const axiosHelper = {
    ajax: async function (url, method, formData, headers={}, isMultipart = false) {
        let response = null;

        if(isMultipart){
            headers['Content-Type'] = 'multipart/form-data';
        }

        try{
            switch (method.toLowerCase()) {
                case 'get':
                    response = await axios.get(url,{
                        params: formData && Object.fromEntries(formData),
                        headers: headers
                    });
                    break;
                case 'post':
                    response = await axios.post(url, formData, {
                        headers: headers
                    });
                    break;
                case 'put':
                    response = await axios.put(url, formData, {
                        headers: headers
                    });
                    break;
                case 'delete':
                    response = await axios.delete(url, {
                        headers: headers
                    });
                    break;
            }
        } catch (error) {
            let alertMsg = null;
            console.log(error);

            if(error.response?.data){
                const data = error.response.data;
                alertMsg = data.message;

                console.error("Error occurred in the backend server.");
                console.error(`[${data.status}] ${data.error}`);
                console.error(data.trace);
            }else{
                alertMsg = error.message;

                console.error("Error occurred in the Axios.");
                console.error(`[${error.code}] ${error.message}`);
            }
            alert(alertMsg);
        }
        return response?.data;
        
    },
    get: async function (url, formData, headers = {}, isMultipart = false) {
        return await this.ajax(url, 'get', formData, headers, isMultipart);
    },
    post: async function (url, formData, headers = {}, isMultipart = false) {
        return await this.ajax(url, 'post', formData, headers, isMultipart);
    },
    put: async function (url, formData, headers = {}, isMultipart = false) {
        return await this.ajax(url, 'put', formData, headers, isMultipart);
    },
    delete: async function (url, headers = {}, isMultipart = false) {
        return await this.ajax(url, 'delete', null, headers, isMultipart);
    },
    getMultipart: async function (url, formData, headers = {}) {
        return await this.get(url, formData, headers, true);
    },
    postMultipart: async function (url, formData, headers = {}) {
        return await this.post(url, formData, headers, true);
    },
    putMultipart: async function (url, formData, headers = {}) {
        return await this.put(url, formData, headers, true);
    },
    deleteMultipart: async function (url, headers = {}) {
        return await this.delete(url, headers, true);
    }
}