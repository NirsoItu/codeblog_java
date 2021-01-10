package com.spring.codeblog.controller;

import com.spring.codeblog.model.Post;
import com.spring.codeblog.service.CodeblogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;

@Controller
public class CodeblogController {

    @Autowired
    CodeblogService codeblogService;

    // Listar todos os posts
    @GetMapping("/posts")
    public String getPosts(Model model) {
        model.addAttribute("posts", codeblogService.findAll());
        return "posts";
    }

    // Listar os detalhes de um post único
    @GetMapping("/posts/{id}")
    public ModelAndView getPostDetails(@PathVariable("id") long id) {
        ModelAndView mv = new ModelAndView("postDetails");
        Post post = codeblogService.findById(id);
        mv.addObject("post", post);
        return mv;
    }

    // Chamar o formulário newpost
    @GetMapping("/newpost")
    public String getPostForm() {
        return "postForm";
    }

    // Salvar o post no banco de dados
    @PostMapping("/newpost")
    public String savePost(@Valid Post post, BindingResult result, RedirectAttributes attributes){
        if(result.hasErrors()){
            return "redirect:/newpost";
        }
        post.setData(LocalDate.now());
        codeblogService.save(post);
        return "redirect:/posts";
    }

    // Abrir a tela de edição com os atributos
    @GetMapping("/editpost/{id}")
    public String preEdit(@PathVariable("id") Long id, Model model) {
        Post post = codeblogService.findById(id);
        model.addAttribute("post", post);
        return "editForm";
    }

    // Salvar a edição do Post
    @PostMapping("/editpost/{id}")
    public String saveEditPost(@PathVariable("id") Long id, @Valid Post post, BindingResult result){
        if (result.hasErrors()) {
            post.setId(id);
            return "editForm";
        }
        post.setData(LocalDate.now());
        codeblogService.save(post);
        return "redirect:/posts";
    }

    // Deletar o post selecionado
    @GetMapping("/deletepost/{id}")
    public String deletePost(@PathVariable("id") long id, Model model) {
        this.codeblogService.deletePostById(id);
        return "redirect:/posts";
    }
}
