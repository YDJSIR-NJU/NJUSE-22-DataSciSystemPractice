import axios from 'axios'
import { QUERY_MODULE } from '@/api/_prefix'

export const rangeFilter = (start, end, column, name) => {
  console.log(`${QUERY_MODULE}/rangeFilter?start=${start}&end=${end}&column=${column}&name=${name}`)
  return axios.get(`${QUERY_MODULE}/rangeFilter?start=${start}&end=${end}&column=${column}&name=${name}`,
    {}).then(res => {
    return res.data
  }).catch(function (error) {
    console.log(error)
  })
}

export const rangeFilterCount = (start, end, column, name) => {
  console.log(`${QUERY_MODULE}/rangeFilterCount?start=${start}&end=${end}&column=${column}&name=${name}`)
  return axios.get(`${QUERY_MODULE}/rangeFilterCount?start=${start}&end=${end}&column=${column}&name=${name}`,
    {}).then(res => {
    return res.data
  }).catch(function (error) {
    console.log(error)
  })
}

export const getByUuid = (uuid) => {
  return axios.get(`${QUERY_MODULE}/uuid?uuid=${uuid}`,
    {}).then(res => {
    return res.data
  }).catch(function (error) {
    console.log(error)
  })
}


