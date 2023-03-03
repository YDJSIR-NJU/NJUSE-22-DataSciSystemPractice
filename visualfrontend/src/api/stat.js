import axios from 'axios'
import { STAT_MODULE } from '@/api/_prefix'

export const filterSum = (column) => {
  return axios.get(`${STAT_MODULE}/filterSum?column=${column}`,
    {}).then(res => {
    return res.data
  }).catch(function (error) {
    console.log(error)
  })
}

export const levelFilterSum = (column, name) => {
  return axios.get(`${STAT_MODULE}/levelFilterSum?column=${column}&name=${name}`,
    {}).then(res => {
    return res.data
  }).catch(function (error) {
    console.log(error)
  })
}

