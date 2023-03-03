import axios from 'axios'
import { LIST_MODULE } from '@/api/_prefix'

export const getEnumName = (column) => {
  return axios.get(`${LIST_MODULE}/enum?column=${column}`,
    {}).then(res => {
    return res.data
  }).catch(function (error) {
    console.log(error)
  })
}

export const getTimeRange = () => {
  return axios.get(`${LIST_MODULE}/timeRange`,
    {}).then(res => {
    return res.data
  }).catch(function (error) {
    console.log(error)
  })
}

