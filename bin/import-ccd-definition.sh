#!/usr/bin/env bash

dir=$(dirname ${0})
root_dir=$(realpath ${dir}/..)

definition_input_dir_name=ccd-definition
definition_output_file_name=ccd-definition.xlsx

definition_input_dir=${root_dir}/${definition_input_dir_name}
definition_output_file=${root_dir}/${definition_output_file_name}

if [[ ! -e ${definition_output_file} ]]; then
   touch ${definition_output_file}
fi

docker run -it --rm --name json2xlsx \
  -v ${definition_input_dir}:/tmp/${definition_input_dir_name} \
  -v ${definition_output_file}:/tmp/${definition_output_file_name} \
  docker.artifactory.reform.hmcts.net/ccd/ccd-definition-processor:latest \
  json2xlsx -D /tmp/${definition_input_dir_name} -o /tmp/${definition_output_file_name}

${dir}/../ccd-docker/bin/ccd-import-definition.sh ${definition_output_file}